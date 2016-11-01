/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.exec.multiobjective.nsgaii;

import java.util.ArrayList;
import java.util.List;
import org.hymo.algorithm.multiobjective.nsgaii.NSGAIIHHBuilder;
import org.hymo.core.heuristicselector.AbstractHeuristicSelector;
import org.hymo.core.heuristicselector.rankedbased.ChoiceFunction;
import org.hymo.core.lowlevelheuristic.CrossoverMutationLowLevelHeuristic;
import org.hymo.core.reward.offspringparent.HITODominanceComparator;
import org.hymo.core.rewardrepository.RankedBased;
import org.hymo.exec.ExperimentAlgorithmRunner;
import static org.hymo.exec.ExperimentAlgorithmRunner.printComputingTime;
import static org.hymo.exec.ExperimentAlgorithmRunner.printFinalSolutionSet;
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
 * @version 1.0
 */
public class NSGAIIHHBinaryRunner extends ExperimentAlgorithmRunner {

    /**
     * @param args Command line arguments.
     * @throws org.uma.jmetal.util.JMetalException
     * @throws java.io.IOException
     * @throws SecurityException
     * @throws ClassNotFoundException Invoking command: java
     * org.uma.jmetal.runner.multiobjective.NSGAIIBinaryRunner problemName
     * [referenceFront]
     */
    public static void main(String[] args) throws Exception {

        BinaryProblem problem;
        Algorithm<List<BinarySolution>> algorithm;
        SelectionOperator<List<BinarySolution>, BinarySolution> selection;
        AbstractHeuristicSelector<BinarySolution> heuristicSelector;

        problem = new OneZeroMax();
        List<CrossoverMutationLowLevelHeuristic<BinarySolution>> llhs = new ArrayList<>();

        double crossoverProbability = 0.9;
        double mutationProbability = 1.0 / problem.getNumberOfBits(0);

        CrossoverOperator singlePoint = new SinglePointCrossover(crossoverProbability);

        MutationOperator nullMutation = new NullMutation();
        MutationOperator bitFlip = new BitFlipMutation(mutationProbability);

        llhs.add(new CrossoverMutationLowLevelHeuristic<>(singlePoint, bitFlip));
        llhs.add(new CrossoverMutationLowLevelHeuristic<>(singlePoint, nullMutation));

        selection = new BinaryTournamentSelection<>();

        heuristicSelector = new ChoiceFunction<>(llhs, new HITODominanceComparator(), new RankedBased(llhs), 1, 0.0005);

        String directory = String.format("results/%s/NSGAIIHH_%s/%s", "OneZeroMax", heuristicSelector.toString(), "EVA_60000_POP_200");

        // Executa o algoritmo
        for (int i = 1; i <= 30; i++) {
            algorithm = new NSGAIIHHBuilder<>(problem, heuristicSelector)
                    .setSelectionOperator(selection)
                    .setMaxEvaluations(60000)
                    .setPopulationSize(200)
                    .build();

            String run = Integer.toString(i);
            System.out.println("Run: " + run);

            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

            List<BinarySolution> population = algorithm.getResult();

            long computingTime = algorithmRunner.getComputingTime();
            
            //Escreve os resultados
            printFinalSolutionSet(population, directory, run);
            printComputingTime(computingTime, directory, run);
            printLowLevelHeuristicsApplications(heuristicSelector.getHistory(), directory, run);
        }
    }
}
