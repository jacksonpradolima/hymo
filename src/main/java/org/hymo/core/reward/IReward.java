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
package org.hymo.core.reward;

import java.io.Serializable;

/**
 * Interface representing a credit assignment
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @param <Source> 
 */
public interface IReward<Source> extends Serializable {

    /**
     * @param parents
     * @param offsprings
     * @param iteration
     * @return Credit value
     */
    public Reward execute(Source parents, Source offsprings, int iteration);
        
    /**
     * @param parents
     * @param offsprings     
     * @return Credit value
     */
    public Reward execute(Source parents, Source offsprings);
}
