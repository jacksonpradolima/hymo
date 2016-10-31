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
package org.hymo.core.lowlevelheuristic;

import java.io.Serializable;

/**
 * Interface representing a low-level heuristic
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @param <S> Source
 * @param <R> Result
 */
public interface ILowLevelHeuristic<S, R> extends Serializable {

    /**
     * @param source The data to process
     * @return 
     */
    public R execute(S source);
}
