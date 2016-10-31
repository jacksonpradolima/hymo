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

import org.hymo.core.reward.Reward;
import java.io.Serializable;

/**
 * This class stores the history of rewards earned by a particular low-level
 * heuristic and your number of applications
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class LLHHistory implements Serializable {

    private static final long serialVersionUID = -2323214225020219554L;

    private ILowLevelHeuristic llh;

    private long appliedCount;

    private Reward reward;

    public LLHHistory(ILowLevelHeuristic llh, long appliedCount, Reward reward) {
        this.llh = llh;
        this.appliedCount = appliedCount;
        this.reward = reward;
    }

    /**
     * @return the appliedCount
     */
    public long getAppliedCount() {
        return appliedCount;
    }

    /**
     * @param appliedCount the appliedCount to set
     */
    public void setAppliedCount(long appliedCount) {
        this.appliedCount = appliedCount;
    }

    /**
     * @return the low-level heuristic
     */
    public ILowLevelHeuristic getLLH() {
        return llh;
    }

    /**
     * @param llh the low-level heuristic to set
     */
    public void setLlh(ILowLevelHeuristic llh) {
        this.llh = llh;
    }

    /**
     * @return the reward
     */
    public Reward getReward() {
        return reward;
    }

    /**
     * @param reward the reward to set
     */
    public void setReward(Reward reward) {
        this.reward = reward;
    }
}
