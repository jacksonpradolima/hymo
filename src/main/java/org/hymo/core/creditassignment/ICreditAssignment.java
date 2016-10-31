/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.core.creditassignment;

import java.util.Map;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.rewardrepository.IRewardRepository;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 */
public interface ICreditAssignment {

    /**
     * Method to get all the credit assignments
     *
     * @return All the the credit assignments
     */
    public Map<ILowLevelHeuristic, Double> getCreditassignments();

    /**
     * Get the LLH's credit assignment value
     * @param llh
     * @return 
     */
    public double getCreditassignment(ILowLevelHeuristic llh);

    /**
     * Apply the credit assignment in the sliding window
     * @param history - Sliding Window
     */
    public void apply(IRewardRepository history);
}
