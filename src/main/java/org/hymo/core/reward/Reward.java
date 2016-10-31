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
 * This class represents a Reward.
 * Reward is the value received when a low-level heuristic is applied (solution's improvement)
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 */
public class Reward implements Comparable<Reward>, Serializable {

    private static final long serialVersionUID = 6693024115485521262L;

    /**
     * The credit value assigned
     */
    protected double value;
    
    /**
     * Constructor assigns value and the iteration t to the credit
     *
     * @param value Value to assign to credit     
     */
    public Reward(double value) {
        this.value = value;        
    }

    /**
     * Returns the value assigned to this credit
     *
     * @return value of this credit
     */
    public double getValue() {
        return value;
    }
   
    @Override
    public int compareTo(Reward o) {
        if (this.equals(o)) {
            return 0;
        }
        return (this.value < o.value) ? -1 : 1;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));        
        return hash;
    }

    /**
     * Returns true if the two credits have the same value irrespective of the
     * iteration.
     *
     * @param obj Should be Credit
     * @return true if the two credits have the same value irrespective of the
     * iteration. Otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reward other = (Reward) obj;
        if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Reward(" + this.value + ")";
    }
}
