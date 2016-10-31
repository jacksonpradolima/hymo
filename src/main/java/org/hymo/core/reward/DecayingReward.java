/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.core.reward;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class DecayingReward {

    /**
     * Decayed factor
     */
    private double decayedFactor;
        
    public DecayingReward(double decayedFactor) {
        this.decayedFactor = decayedFactor;        
    }
    
    public double getDecayedValue(Reward reward, int iteration){
        return Math.pow(decayedFactor, (double)(iteration + 1)) * reward.getValue();
    }
}
