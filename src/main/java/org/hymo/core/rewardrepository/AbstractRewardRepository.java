/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.core.rewardrepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.reward.Reward;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public abstract class AbstractRewardRepository implements IRewardRepository {

    /**
     * Control of counting application
     */
    private HashMap<ILowLevelHeuristic, Long> countApplies;

    public AbstractRewardRepository(List<? extends ILowLevelHeuristic> llhs) {
        this.countApplies = new HashMap<>();

        for (ILowLevelHeuristic llh : llhs) {
            this.countApplies.put(llh, (long) 0);
        }
    }

    @Override
    public void update(ILowLevelHeuristic llh, Reward reward) {
        long countApplications = this.countApplies.get(llh) + 1;
        this.countApplies.put(llh, countApplications);
    }

    @Override
    public Map<ILowLevelHeuristic, Long> getLowLevelHeuristicsApplications() {
        return this.countApplies;
    }

    protected long getCountApplies(ILowLevelHeuristic llh) {
        return this.countApplies.get(llh);
    }        
}
