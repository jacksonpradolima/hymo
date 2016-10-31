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
package org.hymo.core.heuristicselector;

import org.hymo.core.IHyperHeuristic;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.reward.IReward;
import org.hymo.core.rewardrepository.IRewardRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S>
 */
public abstract class AbstractHeuristicSelector<S extends Solution<?>> implements IHyperHeuristic<List<S>> {

    /**
     * The low-level heuristics history (repository)
     */
    protected IRewardRepository history;

    /**
     * The number of times nextHeuristic() is called
     */
    private int iterations;

    /**
     * The low-level heuristics
     */
    protected List<? extends ILowLevelHeuristic> llhs;

    /**
     * Random generator (JMetal)
     */
    private final JMetalRandom randomGenerator;

    /**
     * How will be calculated the reward
     */
    protected IReward reward;

    /***
     * Heuristic Selector Pattern
     * @param llhs
     * @param reward
     * @param history 
     */
    public AbstractHeuristicSelector(List<? extends ILowLevelHeuristic> llhs, IReward reward, IRewardRepository history) {
        this.llhs = llhs;
        this.reward = reward;
        this.history = history;
        this.iterations = 0;
        this.randomGenerator = JMetalRandom.getInstance();
    }

    /**
     *
     * @param llh
     * @return
     */
    protected abstract double evaluateLowLevelHeuristic(ILowLevelHeuristic llh);

    /**
     * A heuristic selector must be override this method and implement the
     * function to evaluate a heuristic.
     *
     * @return the value of the function with the given input
     */
    protected ILowLevelHeuristic getBestHeuristic() {
        ArrayList<ILowLevelHeuristic> ties = new ArrayList();
        ILowLevelHeuristic leadLLH = null;
        double maxVal = Double.NEGATIVE_INFINITY;

        for (ILowLevelHeuristic llh : this.llhs) {
            if (leadLLH == null) {
                leadLLH = llh;
                maxVal = evaluateLowLevelHeuristic(llh);
                continue;
            }
            
            int retval = Double.compare(evaluateLowLevelHeuristic(llh), maxVal);
                        
            if (retval > 0) {
                // if evaluateLowLevelHeuristic(llh) > maxVal
                maxVal = evaluateLowLevelHeuristic(llh);
                leadLLH = llh;
                ties.clear();
            } else if (retval == 0) {
                // if evaluateLowLevelHeuristic(llh) == maxVal
                ties.add(llh);
            }
        }

        if (!ties.isEmpty()) {
            leadLLH = getRandomLowLevelHeuristic(ties);
        }

        return leadLLH;
    }

    /**
     * Gets the heuristics available to the hyper-heuristic
     *
     * @return
     */
    @Override
    public List<? extends ILowLevelHeuristic> getLowLevelHeuristics() {
        return this.llhs;
    }

    /**
     * Get low-level heuristics unplayed
     *
     * @return List of low-level heuristics unplayed (yet)
     */
    protected List<? extends ILowLevelHeuristic> getLowLevelHeuristicsUnplayed() {
        return  new ArrayList<>(this.llhs.stream().filter(heuristic -> history.getCountApplications(heuristic) == 0)
                               .collect(Collectors.toList()));
    }

    /**
     * Selects the next low-level heuristic based on heuristic avaliation in
     * Heuristic Selection. If selection count is zero, a random heuristic is
     * selected with uniform probability
     *
     * @return
     */
    @Override
    public ILowLevelHeuristic getNextLowLevelHeuristic() {
        ILowLevelHeuristic heuristic;
        List<? extends ILowLevelHeuristic> unplayed = getLowLevelHeuristicsUnplayed();

        if (unplayed.isEmpty()) {
            // Get the bestest heuristic for apply
            heuristic = getBestHeuristic();
        } else {
            // If some heuristic was not applied, a random heuristic is selected with uniform probability
            heuristic = getRandomLowLevelHeuristic(unplayed);
        }

        incrementIterations();
        return heuristic;
    }

    /**
     * Returns the number of times nextHeuristic() has been called
     *
     * @return the number of times nextHeuristic() has been called
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Selects randomly a low-level heuristic from a collection of low-level
     * heuristics with uniform probability
     *
     * @param heuristics the collection to draw a random low-level heuristic
     * from
     * @return the randomly selected low-level heuristic
     */
    protected ILowLevelHeuristic getRandomLowLevelHeuristic(List<? extends ILowLevelHeuristic> heuristics) {
        return heuristics.get(randomGenerator.nextInt(0, heuristics.size() - 1));
    }

    /**
     * Increments the number of times nextHeuristic() has been called by one
     */
    protected void incrementIterations() {
        iterations++;
    }
    
    public Map<ILowLevelHeuristic, Long> getHistory(){
        return this.history.getLowLevelHeuristicsApplications();
    }
}
