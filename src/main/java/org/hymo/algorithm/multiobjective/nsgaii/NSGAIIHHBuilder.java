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

import org.hymo.algorithm.HHVariant;
import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
import java.util.List;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S>
 */
public class NSGAIIHHBuilder<S extends Solution<?>> implements AlgorithmBuilder<NSGAIIHH<S>> { 
    private HHVariant variant;

    private SolutionListEvaluator<S> evaluator;
    private final AbstractHeuristicSelector heuristicSelector;
    private int maxEvaluations;
    private int populationSize;
    
    /**
     * NSGAIIHHBuilder class
     */
    private final Problem<S> problem;
    private SelectionOperator<List<S>, S> selectionOperator;

    /**
     * NSGAIIHHBuilder constructor
     *
     * @param problem
     * @param heuristicSelector
     */
    public NSGAIIHHBuilder(Problem<S> problem, AbstractHeuristicSelector heuristicSelector) {
        this.problem = problem;
        maxEvaluations = 25000;
        populationSize = 100;
        this.heuristicSelector = heuristicSelector;
        selectionOperator = new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<>());
        evaluator = new SequentialSolutionListEvaluator<>();

        this.variant = HHVariant.HHInternalReproduction;
    }

    public NSGAIIHHBuilder<S> setVariant(HHVariant variant) {
        this.variant = variant;
        return this;
    }

    @Override
    public NSGAIIHH<S> build() {
        switch (variant) {
            case HHInternalReproduction:
                return new NSGAIIHH<>(problem, maxEvaluations, populationSize, heuristicSelector, selectionOperator, evaluator);
            case HHExternalReproduction:
                return new NSGAIIHHExternalReproduction<>(problem, maxEvaluations, populationSize, heuristicSelector, selectionOperator, evaluator);
            default:
                return new NSGAIIHH<>(problem, maxEvaluations, populationSize, heuristicSelector, selectionOperator, evaluator);
        }
    }

    public AbstractHeuristicSelector getHeuristicSelector() {
        return heuristicSelector;
    }

    public int getMaxIterations() {
        return maxEvaluations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    /* Getters */
    public Problem<S> getProblem() {
        return problem;
    }

    public SelectionOperator<List<S>, S> getSelectionOperator() {
        return selectionOperator;
    }

    public SolutionListEvaluator<S> getSolutionListEvaluator() {
        return evaluator;
    }

    public NSGAIIHHBuilder<S> setMaxEvaluations(int maxEvaluations) {
        if (maxEvaluations < 0) {
            throw new JMetalException("maxEvaluations is negative: " + maxEvaluations);
        }
        this.maxEvaluations = maxEvaluations;

        return this;
    }

    public NSGAIIHHBuilder<S> setPopulationSize(int populationSize) {
        if (populationSize < 0) {
            throw new JMetalException("Population size is negative: " + populationSize);
        }

        this.populationSize = populationSize;

        return this;
    }

    public NSGAIIHHBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
        if (selectionOperator == null) {
            throw new JMetalException("selectionOperator is null");
        }
        this.selectionOperator = selectionOperator;

        return this;
    }

    public NSGAIIHHBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<S> evaluator) {
        if (evaluator == null) {
            throw new JMetalException("evaluator is null");
        }
        this.evaluator = evaluator;

        return this;
    }
}
