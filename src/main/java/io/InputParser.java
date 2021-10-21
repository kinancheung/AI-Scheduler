package io;

import data.State;
import exception.InvalidInputException;

/**
 * Author: Ben Howard, Alex Nicholson, Peter Goedeke
 * Class to parse the arguments given to the application and create an ApplicationState
 * which contains these values
 */
public class InputParser {

    // constructor
    State applicationState;

    public InputParser(State application) {
        applicationState = application;
    }

    /**
     * Parse the arguments to the global application state
     * @param args the arguments to parse
     * @throws InvalidInputException
     */
    public void parse(String[] args) throws InvalidInputException {
        int n = args.length;

        if (n < 2) {
            throw new InvalidInputException("Not enough arguments");
        }
        applicationState.setInputFile(args[0]);

        // Process number of processors
        try {
            int processorAmount = Integer.parseInt(args[1]);
            applicationState.setProcessorAmount(processorAmount);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new InvalidInputException("The provided processor amount is not a valid number");
        }

        for (int i = 2; i < n; i++) {
            // switch over the different possible arguments
            switch (args[i]) {
                case "-v":
                    applicationState.setVisualise(true);
                    
                    break;
                case "-p":
                        applicationState.setParallelize(true);
                        
                        if (i + 1 < n) {
                            i++;
                            try {

                                int cpuCount = Integer.parseInt(args[i]);

                                int maxCpus = Runtime.getRuntime().availableProcessors();

                                // Check if number of cpus is valid
                                if (cpuCount > maxCpus || cpuCount < 1) {
                                    throw new Exception("Invalid count");
                                }

                                applicationState.setParallelizeCount(cpuCount);

                            } catch (Exception e) {
                                throw new InvalidInputException("The provided number of cpus is not valid");
                            }
                           
                        } else {
                            throw new InvalidInputException("Please specify a number of processors");
                        }
                    break;
                case "-o":
                    applicationState.setOutput(true);
                    if (i + 1 < n) {
                        i++;
                        
                        String outputFile = args[i];
                        applicationState.setOutputFileName(outputFile);

                    } else {
                        throw new InvalidInputException("Please enter a valid output file");
                    }
                    break;
            }
        }
    }
    /**
     * Print the help message giving instructions on how to use the command
     */
    public static void printHelp() {
        System.out.println("Usage: java -jar <jarfile> <inputfile> <processorAmount> [-v] [-p <cpuCount>] [-o <outputfile>]");
    }
}
