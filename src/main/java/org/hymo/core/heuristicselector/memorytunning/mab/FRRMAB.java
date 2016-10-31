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
package org.hymo.core.heuristicselector.memorytunning.mab;

import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.reward.IReward;
import org.hymo.core.rewardrepository.IRewardRepository;
import java.util.List;
import org.hymo.core.creditassignment.mab.FRR;
import org.uma.jmetal.solution.Solution;

/**
 * Fitness-Rate-Rank based Multi-Armed Bandit
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S>
 */
public class FRRMAB<S extends Solution<?>> extends AbstractMAB<S> {
  
    /**
     * Fitness-Rate-Rank based Multi-Armed Bandit heuristic selector
     * @param llhs
     * @param reward
     * @param history
     * @param decayedFactor
     * @param c 
     */
    public FRRMAB(List<? extends ILowLevelHeuristic> llhs, IReward reward, IRewardRepository history, double decayedFactor, double c) {
        super(llhs, reward, history, new FRR(llhs, decayedFactor), c);       
    }

    @Override
    protected double evaluateLowLevelHeuristic(ILowLevelHeuristic llh) {
        double frrOp = geCreditassignment(llh);
        long selectedTimes = this.history.getCountApplications(llh);
        double sumApplied = this.history.getReward(llh);

        return frrOp + getC() * Math.sqrt((2 * Math.log(sumApplied)) / selectedTimes);
    }

    @Override
    public void update(ILowLevelHeuristic llh, List<S> parents, List<S> offspring) {
        // My FIR is the calculated using "this.reward.execute" method
        // After the sliding window is updated
        this.history.update(llh, this.reward.execute(parents, offspring, getIterations()));        
        applyCreditAssignment();
    }
            
    @Override
    public String toString() {
        return "FRRMAB";
    }  
}
