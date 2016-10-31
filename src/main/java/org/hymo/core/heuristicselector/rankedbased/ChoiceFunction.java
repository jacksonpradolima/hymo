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
package org.hymo.core.heuristicselector.rankedbased;

import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.reward.IReward;
import org.hymo.core.rewardrepository.IRewardRepository;
import java.util.List;
import org.uma.jmetal.solution.Solution;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S>
 */
public class ChoiceFunction<S extends Solution<?>> extends AbstractHeuristicSelector<S> {

    /**
     * Rank weight.
     */
    private final double alpha;

    /**
     * Elapsed time weight.
     */
    private final double beta;

    /**
     * Choice-Function heuristic selector
     * @param llhs
     * @param reward
     * @param history
     * @param alpha
     * @param beta 
     */
    public ChoiceFunction(List<? extends ILowLevelHeuristic> llhs, IReward reward, IRewardRepository history, double alpha, double beta) {
        super(llhs, reward, history);
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    protected double evaluateLowLevelHeuristic(ILowLevelHeuristic llh) {
        return (alpha * this.history.getReward(llh)) + (beta * (getIterations() / this.history.getCountApplications(llh)));
    }

    @Override
    public void update(ILowLevelHeuristic llh, List<S> parents, List<S> offspring) {
        this.history.update(llh, this.reward.execute(parents, offspring));
    }

    @Override
    public String toString() {
        return "CF";
    }
}
