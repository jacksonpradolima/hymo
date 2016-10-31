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
package org.hymo.core;

import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import java.util.List;

/**
 * Interface to control methods used to select or generate next heuristic(s) to
 * be used in hyper-heuristic
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S>
 */
public interface IHyperHeuristic<S> {

    /**
     * Method to select the low-level heuristic based on some selection method
     *
     * @return the next low-level heuristic to be applied
     */
    public ILowLevelHeuristic getNextLowLevelHeuristic();

    /**
     * Gets the low-level heuristics available
     *
     * @return list of low-level heuristic
     */
    public List<? extends ILowLevelHeuristic> getLowLevelHeuristics();

    /**
     * Method to update the internals of the hyper-heuristic selector based on the given CreditRepository
     *
     * @param llh Low-level heuristic     
     * @param parents     
     * @param offspring     
     */
    public void update(ILowLevelHeuristic llh, S parents, S offspring);
}
