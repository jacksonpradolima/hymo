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

import java.util.ArrayList;
import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
import java.util.List;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S>
 */
@SuppressWarnings("serial")
public class NSGAIIHHExternalReproduction<S extends Solution<?>> extends NSGAIIHH<S> {

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
    public NSGAIIHHExternalReproduction(Problem<S> problem, int maxEvaluations, int populationSize,
            AbstractHeuristicSelector heuristicSelector, SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
        super(problem, maxEvaluations, populationSize, heuristicSelector, selectionOperator, evaluator);
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

        // Select the new LLH
        ILowLevelHeuristic llh = heuristicSelector.getNextLowLevelHeuristic();

        for (int i = 0; i < getMaxPopulationSize(); i += numberOfParents) {
            List<S> parents = new ArrayList<>(numberOfParents);
            for (int j = 0; j < numberOfParents; j++) {
                parents.add(population.get(i + j));
            }

            // Apply the LLH
            List<S> offspring = (List<S>) llh.execute(parents);

            offspringPopulation.addAll(offspring);
        }

        // Update the informations about the LLH's performance
        heuristicSelector.update(llh, population, offspringPopulation);

        return offspringPopulation;
    }
}
