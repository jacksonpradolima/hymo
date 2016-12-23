/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.algorithm.multiobjective.spea2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.spea2.util.EnvironmentalSelection;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.solutionattribute.impl.StrengthRawFitness;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S>
 */
@SuppressWarnings("serial")
public class SPEA2HH<S extends Solution<?>> extends AbstractGeneticAlgorithm<S, List<S>> {

    private static final Logger logger = LoggerFactory.getLogger(SPEA2HH.class);
    
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    protected double percentualProgress;
    protected double percentualProgressAux;

    protected final AbstractHeuristicSelector heuristicSelector;
    protected final int maxIterations;
    protected final SolutionListEvaluator<S> evaluator;
    protected int iterations;
    protected List<S> archive;
    private final StrengthRawFitness<S> strenghtRawFitness = new StrengthRawFitness<>();
    private final EnvironmentalSelection<S> environmentalSelection;

    public SPEA2HH(Problem<S> problem, int maxIterations, int populationSize, int archiveSize,
            AbstractHeuristicSelector heuristicSelector, SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
        super(problem);
        this.maxIterations = maxIterations;
        this.setMaxPopulationSize(populationSize);

        this.heuristicSelector = heuristicSelector;
        this.selectionOperator = selectionOperator;
        this.environmentalSelection = new EnvironmentalSelection<>(populationSize);

        this.archive = new ArrayList<>(archiveSize);

        this.evaluator = evaluator;

        percentualProgress = 0.0;
        percentualProgressAux = 0.0;
    }

    @Override
    protected void initProgress() {
        iterations = 1;
    }

    @Override
    protected void updateProgress() {
        iterations++;
    }

    @Override
    protected boolean isStoppingConditionReached() {
        percentualProgress = (iterations * 100) / maxIterations;
        
        if (percentualProgress != percentualProgressAux) {            
            logger.info(percentualProgress + "% in " + dateFormat.format(new Date()));
            percentualProgressAux = percentualProgress;
        } 
        
        return iterations >= maxIterations;
    }

    @Override
    protected List<S> evaluatePopulation(List<S> population) {
        population = evaluator.evaluate(population, getProblem());
        return population;
    }

    @Override
    protected List<S> selection(List<S> population) {
        List<S> union = new ArrayList<>(2 * getMaxPopulationSize());
        union.addAll(archive);
        union.addAll(population);
        strenghtRawFitness.computeDensityEstimator(union);
        archive = environmentalSelection.execute(union);
        return archive;
    }

    @Override
    protected List<S> reproduction(List<S> population) {
        List<S> offSpringPopulation = new ArrayList<>(getMaxPopulationSize());

        while (offSpringPopulation.size() < getMaxPopulationSize()) {
            List<S> parents = new ArrayList<>(2);
            S candidateFirstParent = selectionOperator.execute(population);
            parents.add(candidateFirstParent);
            S candidateSecondParent;
            candidateSecondParent = selectionOperator.execute(population);
            parents.add(candidateSecondParent);

            // Select the new LLH
            ILowLevelHeuristic llh = heuristicSelector.getNextLowLevelHeuristic();

            // Apply the LLH            
            List<S> offspring = new ArrayList<S>() {
                {
                    add(((List<S>) llh.execute(parents)).get(0));
                }
            };

            // Update the informations about the LLH's performance
            heuristicSelector.update(llh, parents, offspring);

            offSpringPopulation.add(offspring.get(0));
        }
        return offSpringPopulation;
    }

    @Override
    protected List<S> replacement(List<S> population,
            List<S> offspringPopulation) {
        return offspringPopulation;
    }

    @Override
    public List<S> getResult() {
        return archive;
    }

    @Override
    public String getName() {
        return "SPEA2HH";
    }

    @Override
    public String getDescription() {
        return "Strength Pareto Evolutionary Algorithm. Hyper-Heuristic version";
    }
}
