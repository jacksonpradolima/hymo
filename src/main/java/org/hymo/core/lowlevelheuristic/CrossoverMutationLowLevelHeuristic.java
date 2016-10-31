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
package org.hymo.core.lowlevelheuristic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.Solution;

/**
 * Crossover and Mutation how Low-Level Heuristic
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S> Solution
 */
public class CrossoverMutationLowLevelHeuristic<S extends Solution<?>> implements ILowLevelHeuristic<List<S>, List<S>> {

    /**
     * Crossover operator
     */
    private CrossoverOperator<S> crossoverOperator;

    /**
     * Mutation operator
     */
    private MutationOperator<S> mutationOperator;

    /**
     * Low-level heuristic that uses crossover and mutation
     * @param crossover
     * @param mutation 
     */
    public CrossoverMutationLowLevelHeuristic(CrossoverOperator crossover, MutationOperator mutation) {
        this.crossoverOperator = crossover;
        this.mutationOperator = mutation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final CrossoverMutationLowLevelHeuristic other = (CrossoverMutationLowLevelHeuristic) obj;

        if (!Objects.equals(this.crossoverOperator, other.crossoverOperator)) {
            return false;
        }

        if (!Objects.equals(this.mutationOperator, other.mutationOperator)) {
            return false;
        }

        return true;
    }

    @Override
    public List<S> execute(List<S> source) {
        List<S> offspring = getCrossoverOperator().execute(source);

        List<S> offspringPopulation = new ArrayList<>();

        for (S s : offspring) {
            getMutationOperator().execute(s);
            offspringPopulation.add(s);
        }

        return offspringPopulation;
    }

    /**
     * @return the crossoverOperator
     */
    public CrossoverOperator<S> getCrossoverOperator() {
        return crossoverOperator;
    }

    /**
     * @param crossoverOperator the crossoverOperator to set
     */
    public void setCrossoverOperator(CrossoverOperator<S> crossoverOperator) {
        this.crossoverOperator = crossoverOperator;
    }

    /**
     * @return the mutationOperator
     */
    public MutationOperator<S> getMutationOperator() {
        return mutationOperator;
    }

    /**
     * @param mutationOperator the mutationOperator to set
     */
    public void setMutationOperator(MutationOperator<S> mutationOperator) {
        this.mutationOperator = mutationOperator;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.getCrossoverOperator());
        hash = 89 * hash + Objects.hashCode(this.getMutationOperator());
        return hash;
    }

    @Override
    public String toString() {
        return String.format("Crossover(%s) - Mutation(%s)",
                getCrossoverOperator().getClass().getSimpleName(),
                getMutationOperator().getClass().getSimpleName());
    }
}
