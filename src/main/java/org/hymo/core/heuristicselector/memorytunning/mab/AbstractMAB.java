/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.core.heuristicselector.memorytunning.mab;

import java.util.List;
import org.hymo.core.creditassignment.ICreditAssignment;
import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.reward.IReward;
import org.hymo.core.rewardrepository.IRewardRepository;
import org.uma.jmetal.solution.Solution;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S>
 */
public abstract class AbstractMAB<S extends Solution<?>> extends AbstractHeuristicSelector<S> {

    protected ICreditAssignment creditAssignment;
    
    /**
     * Scaling Factor
     */
    private double c;

    /***
     * Pattern mab procedures
     * @param llhs
     * @param reward
     * @param history
     * @param creditassignment
     * @param c 
     */
    public AbstractMAB(List<? extends ILowLevelHeuristic> llhs, IReward reward, IRewardRepository history, ICreditAssignment creditassignment, double c) {
        super(llhs, reward, history);
        this.creditAssignment = creditassignment;       
        this.c = c;
    }

    /**
     * @return the c
     */
    public double getC() {
        return c;
    }

    /**
     * @param c the c to set
     */
    public void setC(double c) {
        this.c = c;
    }

    protected void applyCreditAssignment() {
        this.creditAssignment.apply(this.history);
    }

    protected double geCreditassignment(ILowLevelHeuristic llh) {
        return this.creditAssignment.getCreditassignment(llh);
    }
}
