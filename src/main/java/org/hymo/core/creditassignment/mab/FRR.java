/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.core.creditassignment.mab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.hymo.core.creditassignment.AbstractCreditAssignment;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.lowlevelheuristic.LLHHistory;
import org.hymo.core.qualityestimation.SumRewards;
import org.hymo.core.reward.DecayingReward;
import org.hymo.core.reward.Reward;
import org.hymo.core.rewardrepository.IRewardRepository;

/**
 * Fitness-Rate-Rank Credit assignment
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class FRR extends AbstractCreditAssignment {

    /**
     * Decayed factor
     */
    private DecayingReward decayingReward;

    /**
     * Fitness-Rate-Rank Credit assignment procedure
     * @param llhs
     * @param decayedFactor
     */
    public FRR(List<? extends ILowLevelHeuristic> llhs, double decayedFactor) {
        super(llhs);

        this.decayingReward = new DecayingReward(decayedFactor);
    }

    @Override
    public void apply(IRewardRepository history) {
        // Now, I must calculate the sum of the rewards (FIRs) by each operator in the sliding window
        Map<ILowLevelHeuristic, Double> sumRewards = new SumRewards().estimate(history);

        //find rank of each low-level heuristic
        ArrayList<LLHHistory> rankRewards = new ArrayList<>(sumRewards.entrySet()
                .stream()
                .sorted(Map.Entry.<ILowLevelHeuristic, Double>comparingByValue().reversed())
                .map(m -> new LLHHistory(m.getKey(), 0, new Reward(m.getValue())))
                .collect(Collectors.toList()));

        // compute decay values
        HashMap<ILowLevelHeuristic, Double> decay = new HashMap<>();
        double sumDecay = 0;

        for (int i = 0; i < rankRewards.size(); i++) {
            LLHHistory currentReward = rankRewards.get(i);
            double currentDecay = getDecayedReward().getDecayedValue(currentReward.getReward(), i);
            decay.put(currentReward.getLLH(), currentDecay);

            sumDecay += currentDecay;
        }

        // compute FRR
        for (ILowLevelHeuristic llh : this.llhs) {
            double value = 0;

            if (decay.containsKey(llh)) {
                value = decay.get(llh) / sumDecay;
            }

            updateCreditAssignment(llh, value);
        }
    }

    /**
     * @return the decayedFactor
     */
    public DecayingReward getDecayedReward() {
        return decayingReward;
    }

    /**
     * @param decayedFactor the decayedFactor to set
     */
    public void setDecayedFactor(double decayedFactor) {
        this.decayingReward = new DecayingReward(decayedFactor);
    }
}
