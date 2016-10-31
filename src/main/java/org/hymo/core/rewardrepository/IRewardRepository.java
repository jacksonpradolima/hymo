/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.core.rewardrepository;

import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.lowlevelheuristic.LLHHistory;
import org.hymo.core.reward.Reward;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interface to store the reward histories of multiple low-level heuristics
 *
 * @author Prado Lima
 */
public interface IRewardRepository {

    /**
     * Clears the reward stored in the repository
     */
    public void clear();

    /**
     * Gets the heuristic that was rewarded the most recently
     *
     * @param llh
     * @return
     */
    public long getCountApplications(ILowLevelHeuristic llh);
    
    /**
     * Gets the number applications of each heuristic
     *     
     * @return
     */
    public Map<ILowLevelHeuristic, Long> getLowLevelHeuristicsApplications();

    /**
     * Gets the history of a low-level heuristic and yours LLHHistory (LLH,
     * Reward, Applied Count)
     *
     * @return
     */
    public Map<ILowLevelHeuristic, ArrayList<LLHHistory>> getHistory();

    /**
     * gets the most recent credit in the repository for a specified heuristic
     *
     * @param llh of interest
     * @return the most recent credit in the repository for the specified
     * heuristic
     */
    public double getReward(ILowLevelHeuristic llh);

    /**
     * Updates the reward history for the specified reward
     *
     * @param llh
     * @param reward
     */
    public void update(ILowLevelHeuristic llh, Reward reward);   
}
