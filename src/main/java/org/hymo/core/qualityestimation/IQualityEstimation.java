/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.core.qualityestimation;

import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.rewardrepository.IRewardRepository;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Prado Lima
 */
public interface IQualityEstimation extends Serializable {

    /**
     * estimates the quality of all heuristics based on its rewards history
     *
     * @param repository the repository that stores all the reward histories
     * @return The estimated quality
     */
    public Map<ILowLevelHeuristic, Double> estimate(IRewardRepository repository);
}
