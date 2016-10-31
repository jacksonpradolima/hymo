/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.core.creditassignment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public abstract class AbstractCreditAssignment implements ICreditAssignment {

    /**
     * The low-level heuristics
     */
    protected List<? extends ILowLevelHeuristic> llhs;

    protected HashMap<ILowLevelHeuristic, Double> historyCreditassignment;

    /**
     * Credit assignment Procedure Pattern     
     * @param llhs 
     */
    public AbstractCreditAssignment(List<? extends ILowLevelHeuristic> llhs) {
        this.historyCreditassignment = new HashMap<>();
        this.llhs = llhs;

        for (ILowLevelHeuristic llh : llhs) {
            historyCreditassignment.put(llh, (double) 1);
        }

    }

    @Override
    public double getCreditassignment(ILowLevelHeuristic llh) {
        return this.historyCreditassignment.get(llh);
    }

    @Override
    public Map<ILowLevelHeuristic, Double> getCreditassignments() {
        return this.historyCreditassignment;
    }

    protected void updateCreditAssignment(ILowLevelHeuristic llh, double value) {
        this.historyCreditassignment.put(llh, value);
    }
}
