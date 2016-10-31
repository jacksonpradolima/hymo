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
package org.hymo.core.rewardrepository;

import java.util.ArrayList;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.lowlevelheuristic.LLHHistory;
import org.hymo.core.reward.Reward;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hymo.core.lowlevelheuristic.LLHHistory;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class RankedBased extends AbstractRewardRepository {

    private HashMap<ILowLevelHeuristic, LLHHistory> ranking;

    public RankedBased(List<? extends ILowLevelHeuristic> llhs) {
        super(llhs);
        this.ranking = new HashMap<>();

        for (ILowLevelHeuristic llh : llhs) {
            this.ranking.put(llh, new LLHHistory(llh, 0, new Reward(1)));
        }
    }

    @Override
    public void clear() {
        this.ranking.entrySet().stream().forEach(h -> this.ranking.put(h.getKey(), new LLHHistory(h.getKey(), 0, new Reward(1))));
    }

    @Override
    public long getCountApplications(ILowLevelHeuristic llh) {
        return this.ranking.get(llh).getAppliedCount();
    }

    @Override
    public Map<ILowLevelHeuristic, ArrayList<LLHHistory>> getHistory() {
        HashMap<ILowLevelHeuristic, ArrayList<LLHHistory>> history = new HashMap();

        this.ranking.entrySet().stream().forEach(r -> {
            ArrayList<LLHHistory> list = new ArrayList<>();
            list.add(r.getValue());

            history.put(r.getKey(), list);
        });

        return history;
    }

    @Override
    public double getReward(ILowLevelHeuristic llh) {
        return this.ranking.get(llh).getReward().getValue();
    }

    @Override
    public void update(ILowLevelHeuristic llh, Reward reward) {
        super.update(llh, reward);
        this.ranking.put(llh, new LLHHistory(llh, getCountApplications(llh) + (long) 1, reward));
    }
}
