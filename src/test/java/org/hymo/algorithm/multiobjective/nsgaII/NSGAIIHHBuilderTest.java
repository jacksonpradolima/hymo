/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.algorithm.multiobjective.nsgaII;

import org.hymo.algorithm.multiobjective.nsgaii.NSGAIIHHBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertTrue;
import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
import org.hymo.core.heuristicselector.memorytunning.mab.FRRMAB;
import org.hymo.core.heuristicselector.random.Random;
import org.hymo.core.heuristicselector.rankedbased.ChoiceFunction;
import org.hymo.core.lowlevelheuristic.CrossoverMutationLowLevelHeuristic;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.hymo.core.reward.offspringparent.HITODominanceComparator;
import org.hymo.core.rewardrepository.RankedBased;
import org.hymo.core.rewardrepository.SlidingWindow;
import org.junit.Test;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.mutation.NullMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.multiobjective.OneZeroMax;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 */
public class NSGAIIHHBuilderTest {

    BinaryProblem problem;
    Algorithm<List<BinarySolution>> algorithm;
    SelectionOperator<List<BinarySolution>, BinarySolution> selection;
    AbstractHeuristicSelector<BinarySolution> heuristicSelector;
    List<CrossoverMutationLowLevelHeuristic<BinarySolution>> llhs;

    public NSGAIIHHBuilderTest() {
        problem = new OneZeroMax();
        selection = new BinaryTournamentSelection<>();
        llhs = new ArrayList<>();

        double crossoverProbability = 0.9;
        double mutationProbability = 1.0 / problem.getNumberOfBits(0);

        CrossoverOperator singlePoint = new SinglePointCrossover(crossoverProbability);

        MutationOperator nullMutation = new NullMutation();
        MutationOperator bitFlip = new BitFlipMutation(mutationProbability);

        llhs.add(new CrossoverMutationLowLevelHeuristic<>(singlePoint, bitFlip));
        llhs.add(new CrossoverMutationLowLevelHeuristic<>(singlePoint, nullMutation));
    }

    public void apply() {
        algorithm = new NSGAIIHHBuilder<>(problem, heuristicSelector)
                .setSelectionOperator(selection)
                .setMaxEvaluations(60000)
                .setPopulationSize(200)
                .build();

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

        List<BinarySolution> population = algorithm.getResult();

        long computingTime = algorithmRunner.getComputingTime();

        System.out.println("====================================================");
        System.out.println("Heuristic Selector: " + heuristicSelector.toString());

        System.out.println("Computing time: " + computingTime);

        System.out.println("LLHs applications (LLH;Count):");

        for (Map.Entry<ILowLevelHeuristic, Long> entry : heuristicSelector.getHistory().entrySet()) {
            System.out.println(entry.getKey().toString() + ";" + entry.getValue());
        }

//        System.out.println("Results (Objective1;Objective2 - Variable) \n");
//
//        for (BinarySolution binarySolution : population) {
//            String objectives = binarySolution.getObjective(0) + ";" + binarySolution.getObjective(1);
//            System.out.println(objectives + " - " + binarySolution.getVariableValue(0));
//        }       
    }

    @Test
    public void testCF() {
        try {
            heuristicSelector = new ChoiceFunction<>(llhs, new HITODominanceComparator(), new RankedBased(llhs), 1, 0.0005);
            apply();

            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testFRRMAB() {
        try {
            heuristicSelector = new FRRMAB<>(llhs, new HITODominanceComparator(), new SlidingWindow(llhs, 50), 1, 0.5);
            apply();

            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testRandom() {
        try {
            heuristicSelector = new Random<>(llhs, new HITODominanceComparator(), new RankedBased(llhs));
            apply();

            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
