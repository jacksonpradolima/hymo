/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hymo.exec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hymo.core.lowlevelheuristic.ILowLevelHeuristic;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class ExperimentAlgorithmRunner extends AbstractAlgorithmRunner {

    /**
     * Write the population into two files and prints some data on screen
     *
     * @param population
     * @param directory
     * @param run
     */
    public static void printFinalSolutionSet(List<? extends Solution<?>> population, String directory, String run) {
        // Create the directory if it doesn't exist
        createDirectory(directory);

        String varFileName = String.format("%s%sVAR_%s", directory, File.separator, run);
        String funFileName = String.format("%s%sFUN_%s", directory, File.separator, run);

        new SolutionListOutput(population)
                .setSeparator("\t")
                .setVarFileOutputContext(new DefaultFileOutputContext(varFileName))
                .setFunFileOutputContext(new DefaultFileOutputContext(funFileName))
                .print();
    }

    public static void printComputingTime(long computingTime, String directory, String run) throws IOException {
        // Create the directory if it doesn't exist
        createDirectory(directory);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(String.format("%s%sTIME_%s", directory, File.separator, run)), false))) {
            writer.write(computingTime + "");
        }
    }

    public static void printLowLevelHeuristicsApplications(Map<ILowLevelHeuristic, Long> history, String directory, String run) throws IOException {
        // Create the directory if it doesn't exist
        createDirectory(directory);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(String.format("%s%sLLH_Applications_%s", directory, File.separator, run)), false))) {
            for (Map.Entry<ILowLevelHeuristic, Long> entry : history.entrySet()) {
                writer.write(entry.getKey().toString() + ";" + entry.getValue());
                writer.write(System.lineSeparator());
            }
        }
    }

    public static void createDirectory(String dir) {
        File experimentDirectory;
        experimentDirectory = new File(dir);

        if (!experimentDirectory.exists()) {
            experimentDirectory.mkdirs();
        }
    }
}
