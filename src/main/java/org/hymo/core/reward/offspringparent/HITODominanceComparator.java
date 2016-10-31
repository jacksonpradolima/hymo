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
package org.hymo.core.reward.offspringparent;

import java.util.List;
import org.hymo.core.reward.IReward;
import org.hymo.core.reward.Reward;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.DominanceComparator;

/**
 * Implementation of Dominance Comparator used in the HITO. This version is
 * based in the paper: G. Guizzo, G. M. Fritsche, S. R. Vergilio, and A. T. R.
 * Pozo. A hyper-heuristic for the multi-objective integration and test order
 * problem. Proceedings of the 2015 on Genetic and Evolutionary Computation
 * Conference, pages 1343–1350. ACM, 2015.
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 * @param <S> Solution
 */
public class HITODominanceComparator<S extends Solution<?>> implements IReward<List<S>> {

    private final DominanceComparator<S> jmetalDominanceComparator = new DominanceComparator<>();

    @Override
    public Reward execute(List<S> parents, List<S> offsprings, int iteration) {
        return execute(parents, offsprings);
    }

    @Override
    public Reward execute(List<S> parents, List<S> offsprings) {
        double value = 0;

        for (S parent : parents) {
            for (S offspring : offsprings) {
                value += (jmetalDominanceComparator.compare(parent, offspring) + 1) / 2;
            }
        }

        value /= (parents.size() * offsprings.size());

        return new Reward(value);
    }
}
