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
package org.hymo.core.qualityestimation;

import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.lowlevelheuristic.LLHHistory;
import org.hymo.core.rewardrepository.IRewardRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class SumRewards implements IQualityEstimation {

    @Override
    public Map<ILowLevelHeuristic, Double> estimate(IRewardRepository repository) {
        HashMap<ILowLevelHeuristic, Double> result = new HashMap<>();

        Map<ILowLevelHeuristic, ArrayList<LLHHistory>> history = repository.getHistory();

        // Best way to improve the time
        for (Map.Entry<ILowLevelHeuristic, ArrayList<LLHHistory>> entry : history.entrySet()) {
            ILowLevelHeuristic key = entry.getKey();
            ArrayList<LLHHistory> value = entry.getValue();

            double sum = 0;

            for (LLHHistory lLHHistory : value) {
                sum += lLHHistory.getReward().getValue();
            }

            result.put(key, sum);
        }

        return result;
    }
}
