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
package org.hymo.core.heuristicselector.random;

import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.reward.IReward;
import org.hymo.core.rewardrepository.IRewardRepository;
import java.util.List;
import org.uma.jmetal.solution.Solution;

/**
 * Selects a low-level heuristic randomly
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S> Solution
 */
public class Random<S extends Solution<?>> extends AbstractHeuristicSelector<S> {

    /**
     * Random heuristic selector
     * @param llhs
     * @param reward
     * @param history 
     */
    public Random(List llhs, IReward reward, IRewardRepository history) {
        super(llhs, reward, history);
    }

    @Override
    public ILowLevelHeuristic getNextLowLevelHeuristic() {
        ILowLevelHeuristic heuristic = getRandomLowLevelHeuristic(getLowLevelHeuristics());

        incrementIterations();

        return heuristic;
    }

    @Override
    protected double evaluateLowLevelHeuristic(ILowLevelHeuristic llh) {
        // Never will be used
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(ILowLevelHeuristic llh, List<S> parents, List<S> offspring) {
        this.history.update(llh, this.reward.execute(parents, offspring, getIterations()));
    }

    @Override
    public String toString() {
        return "Random";
    }
}
