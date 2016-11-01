/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.algorithm.multiobjective.spea2;

import java.util.List;
import org.hymo.algorithm.HHVariant;
import org.hymo.algorithm.multiobjective.nsgaii.NSGAIIHH;
import org.hymo.algorithm.multiobjective.nsgaii.NSGAIIHHBuilder;
import org.hymo.algorithm.multiobjective.nsgaii.NSGAIIHHExternalReproduction;
import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S>
 */
public class SPEA2HHBuilder<S extends Solution<?>> implements AlgorithmBuilder<SPEA2HH<S>> {

    private HHVariant variant;

    /**
     * SPEA2HHBuilder class
     */
    protected final Problem<S> problem;
    protected int maxIterations;
    protected int populationSize;
    protected int archiveSize;
    private final AbstractHeuristicSelector heuristicSelector;
    protected SelectionOperator<List<S>, S> selectionOperator;
    protected SolutionListEvaluator<S> evaluator;

    /**
     * SPEA2Builder constructor
     *
     * @param problem
     * @param heuristicSelector
     */
    public SPEA2HHBuilder(Problem<S> problem, AbstractHeuristicSelector heuristicSelector) {
        this.problem = problem;
        maxIterations = 250;
        populationSize = 100;
        archiveSize = populationSize;
        selectionOperator = new BinaryTournamentSelection<>();
        evaluator = new SequentialSolutionListEvaluator<>();

        this.heuristicSelector = heuristicSelector;
        this.variant = HHVariant.HHInternalReproduction;
    }

    public SPEA2HHBuilder<S> setMaxIterations(int maxIterations) {
        if (maxIterations < 0) {
            throw new JMetalException("maxIterations is negative: " + maxIterations);
        }
        this.maxIterations = maxIterations;

        return this;
    }

    public SPEA2HHBuilder<S> setPopulationSize(int populationSize) {
        if (populationSize < 0) {
            throw new JMetalException("Population size is negative: " + populationSize);
        }

        this.populationSize = populationSize;

        return this;
    }

    public SPEA2HHBuilder<S> setArchiveSize(int archiveSize) {
        if (archiveSize < 0) {
            throw new JMetalException("Archive size is negative: " + archiveSize);
        }

        this.archiveSize = archiveSize;

        return this;
    }

    public SPEA2HHBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
        if (selectionOperator == null) {
            throw new JMetalException("selectionOperator is null");
        }
        this.selectionOperator = selectionOperator;

        return this;
    }

    public SPEA2HHBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<S> evaluator) {
        if (evaluator == null) {
            throw new JMetalException("evaluator is null");
        }
        this.evaluator = evaluator;

        return this;
    }

    public SPEA2HHBuilder<S> setVariant(HHVariant variant) {
        this.variant = variant;
        return this;
    }

    public SPEA2HH<S> build() {
        switch (variant) {
            case HHInternalReproduction:
                return new SPEA2HH<>(problem, maxIterations, populationSize, archiveSize, heuristicSelector, selectionOperator, evaluator);
            case HHExternalReproduction:
                return new SPEA2HH<>(problem, maxIterations, populationSize, archiveSize, heuristicSelector, selectionOperator, evaluator);
            default:
                return new SPEA2HH<>(problem, maxIterations, populationSize, archiveSize, heuristicSelector, selectionOperator, evaluator);
        }
    }

    /* Getters */
    public Problem<S> getProblem() {
        return problem;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public int getPopulationSize() {
        return populationSize;
    }
    
    public int getArchiveSize() {
        return archiveSize;
    }
    
    public SelectionOperator<List<S>, S> getSelectionOperator() {
        return selectionOperator;
    }

    public SolutionListEvaluator<S> getSolutionListEvaluator() {
        return evaluator;
    }
}
