/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.algorithm.multiobjective.spea2;

import java.util.ArrayList;
import java.util.List;
import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
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
public class SPEA2HHExternalReproduction<S extends Solution<?>> extends SPEA2HH<S> {

    public SPEA2HHExternalReproduction(Problem<S> problem, int maxIterations, int populationSize, int archiveSize,
            AbstractHeuristicSelector heuristicSelector, SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
        super(problem, maxIterations, populationSize, archiveSize, heuristicSelector, selectionOperator, evaluator);
    }

    @Override
    protected List<S> reproduction(List<S> population) {
        List<S> offSpringPopulation = new ArrayList<>(getMaxPopulationSize());

        // Select the new LLH
        ILowLevelHeuristic llh = heuristicSelector.getNextLowLevelHeuristic();

        while (offSpringPopulation.size() < getMaxPopulationSize()) {
            List<S> parents = new ArrayList<>(2);
            S candidateFirstParent = selectionOperator.execute(population);
            parents.add(candidateFirstParent);
            S candidateSecondParent;
            candidateSecondParent = selectionOperator.execute(population);
            parents.add(candidateSecondParent);

            // Apply the LLH            
            List<S> offspring = new ArrayList<S>() {
                {
                    add(((List<S>) llh.execute(parents)).get(0));
                }
            };

            offSpringPopulation.add(offspring.get(0));
        }

        // Update the informations about the LLH's performance
        heuristicSelector.update(llh, population, offSpringPopulation);

        return offSpringPopulation;
    }
}
