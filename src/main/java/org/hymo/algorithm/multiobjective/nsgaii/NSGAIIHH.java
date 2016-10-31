/*
 * Copyright 2016 Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hymo.algorithm.multiobjective.nsgaii;

import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.comparator.CrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.CrowdingDistance;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S>
 */
@SuppressWarnings("serial")
public class NSGAIIHH<S extends Solution<?>> extends AbstractGeneticAlgorithm<S, List<S>> {

    protected int evaluations;
    protected final SolutionListEvaluator<S> evaluator;

    protected final AbstractHeuristicSelector heuristicSelector;

    protected final int maxEvaluations;

    /**
     * Constructor
     *
     * @param problem
     * @param maxEvaluations
     * @param populationSize
     * @param heuristicSelector
     * @param selectionOperator
     * @param evaluator
     */
    public NSGAIIHH(Problem<S> problem, int maxEvaluations, int populationSize,
            AbstractHeuristicSelector heuristicSelector, SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
        super(problem);
        this.maxEvaluations = maxEvaluations;
        setMaxPopulationSize(populationSize);

        this.heuristicSelector = heuristicSelector;
        this.selectionOperator = selectionOperator;

        this.evaluator = evaluator;
    }

    protected void addLastRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S> population) {
        List<S> currentRankedFront = ranking.getSubfront(rank);

        Collections.sort(currentRankedFront, new CrowdingDistanceComparator<S>());

        int i = 0;
        while (population.size() < getMaxPopulationSize()) {
            population.add(currentRankedFront.get(i));
            i++;
        }
    }

    protected void addRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S> population) {
        List<S> front;

        front = ranking.getSubfront(rank);

        for (S solution : front) {
            population.add(solution);
        }
    }

    protected Ranking<S> computeRanking(List<S> solutionList) {
        Ranking<S> ranking = new DominanceRanking<>();
        ranking.computeRanking(solutionList);

        return ranking;
    }

    protected List<S> crowdingDistanceSelection(Ranking<S> ranking) {
        CrowdingDistance<S> crowdingDistance = new CrowdingDistance<>();
        List<S> population = new ArrayList<>(getMaxPopulationSize());
        int rankingIndex = 0;
        while (populationIsNotFull(population)) {
            crowdingDistance.computeDensityEstimator(ranking.getSubfront(rankingIndex));
            if (subfrontFillsIntoThePopulation(ranking, rankingIndex, population)) {
                addRankedSolutionsToPopulation(ranking, rankingIndex, population);
                rankingIndex++;
            } else {
                addLastRankedSolutionsToPopulation(ranking, rankingIndex, population);
            }
        }

        return population;
    }

    @Override
    protected List<S> evaluatePopulation(List<S> population) {
        population = evaluator.evaluate(population, getProblem());

        return population;
    }

    @Override
    public String getDescription() {
        return "Nondominated Sorting Genetic Algorithm version II. Hyper-Heuristic version";
    }

    @Override
    public String getName() {
        return "NSGAIIHH";
    }

    protected List<S> getNonDominatedSolutions(List<S> solutionList) {
        return SolutionListUtils.getNondominatedSolutions(solutionList);
    }

    @Override
    public List<S> getResult() {
        return getNonDominatedSolutions(getPopulation());
    }

    @Override
    protected void initProgress() {
        evaluations = getMaxPopulationSize();
    }

    @Override
    protected boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }

    protected boolean populationIsNotFull(List<S> population) {
        return population.size() < getMaxPopulationSize();
    }

    @Override
    protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
        List<S> jointPopulation = new ArrayList<>();
        jointPopulation.addAll(population);
        jointPopulation.addAll(offspringPopulation);

        Ranking<S> ranking = computeRanking(jointPopulation);

        return crowdingDistanceSelection(ranking);
    }

    /**
     * This methods iteratively applies a {@link CrossoverOperator} a
     * {@link MutationOperator} to the population to create the offspring
     * population. The population size must be divisible by the number of
     * parents required by the {@link CrossoverOperator}; this way, the needed
     * parents are taken sequentially from the population.
     *
     * No limits are imposed to the number of solutions returned by the
     * {@link CrossoverOperator}.
     *
     * @param population
     * @return The new created offspring population
     */
    @Override
    protected List<S> reproduction(List<S> population) {

        int numberOfParents = 2;

        checkNumberOfParents(population, numberOfParents);

        List<S> offspringPopulation = new ArrayList<>(getMaxPopulationSize());
        for (int i = 0; i < getMaxPopulationSize(); i += numberOfParents) {                        
            List<S> parents = new ArrayList<>(numberOfParents);
            for (int j = 0; j < numberOfParents; j++) {
                parents.add(population.get(i + j));
            }

            // Select the new LLH
            ILowLevelHeuristic llh = heuristicSelector.getNextLowLevelHeuristic();            
            // Apply the LLH
            List<S> offspring = (List<S>) llh.execute(parents);
            // Update the informations about the LLH's performance
            heuristicSelector.update(llh, parents, offspring);

            offspringPopulation.addAll(offspring);
        }
        return offspringPopulation;
    }

    protected boolean subfrontFillsIntoThePopulation(Ranking<S> ranking, int rank, List<S> population) {
        return ranking.getSubfront(rank).size() < (getMaxPopulationSize() - population.size());
    }

    @Override
    protected void updateProgress() {
        evaluations += getMaxPopulationSize();
    }
}
