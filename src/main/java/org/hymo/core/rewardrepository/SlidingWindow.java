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

import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.lowlevelheuristic.LLHHistory;
import org.hymo.core.reward.Reward;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class SlidingWindow extends AbstractRewardRepository {
    
    private LinkedList<LLHHistory> slidingWindow;
  
    private final int windowSize;
    
    public SlidingWindow(List<? extends ILowLevelHeuristic> llhs, int windowSize) {
        super(llhs);
        this.windowSize = windowSize;
        this.slidingWindow = new LinkedList<>();
        
        for (ILowLevelHeuristic llh : llhs) {
            this.slidingWindow.add(new LLHHistory(llh, 0, new Reward(1)));            
        }
    }
    
    @Override
    public void clear() {
        this.slidingWindow.stream().forEach(h -> {
            this.slidingWindow.add(new LLHHistory(h.getLLH(), 0, new Reward(1)));
        });
    }
    
    @Override
    public long getCountApplications(ILowLevelHeuristic llh) {
        //return this.slidingWindow.stream().filter(f -> f.getLLH().equals(llh)).count();

        // this form count faster
        long count = 0;
        
        for (LLHHistory history : slidingWindow) {
            if (history.getLLH().equals(llh)) {
                ++count;
            }
        }
        
        return count;
    }
    
    @Override
    public HashMap<ILowLevelHeuristic, ArrayList<LLHHistory>> getHistory() {
        HashMap<ILowLevelHeuristic, ArrayList<LLHHistory>> history = new HashMap();
        
        this.slidingWindow.stream().forEach(r -> {
            ArrayList<LLHHistory> llhHistory = new ArrayList<>();
            
            if (history.containsKey(r.getLLH())) {
                llhHistory = new ArrayList(history.get(r.getLLH()));
            }
            
            llhHistory.add(r);
            
            history.put(r.getLLH(), llhHistory);
        });
        
        return history;
    }
    
    @Override
    public double getReward(ILowLevelHeuristic llh) {
        /*double sum = 0;

        for (LLHHistory history : slidingWindow) {
            if (history.getLLH().equals(llh)) {
                sum += history.getReward().getValue();
            }
        }
        
        return sum;*/
        return this.slidingWindow.stream().filter(f -> f.getLLH().equals(llh)).mapToDouble(m -> m.getReward().getValue()).sum();
    }
    
    @Override
    public void update(ILowLevelHeuristic llh, Reward reward) {
        super.update(llh, reward);        
        
        this.slidingWindow.addLast(new LLHHistory(llh, getCountApplies(llh), reward));

        // Truncate
        if (slidingWindow.size() > windowSize) {
            slidingWindow.removeFirst();
        }
    }
}
