package io;

import data.State;

/**
 * Author: Alex Nicholson
 * Date: 08/08/2021
 *
 * This is class mocks the application state of the algorithm to provide supports for testing the InputParser Class.
 */

public class MockState implements State{
    
    public String inputFile;
    public int processorAmount;

    public Boolean visualise;
    public Boolean parallelize;
    public int parallelizeCount;
    public Boolean output;
    public String outputFileName;

    public MockState() {
        // default state information
        inputFile = "";
        processorAmount = 0;
        visualise = false;
        parallelize = false;
        parallelizeCount = 0;
        output = false;
        outputFileName = "";
    }

    public Boolean getVisualise() {
        return visualise;
    }

    public void setVisualise(Boolean visualise) {
        this.visualise = visualise;
    }

    public Boolean getParallelize() {
        return parallelize;
    }

    public void setParallelize(Boolean parallelize) {
        this.parallelize = parallelize;
    }

    public int getParallelizeCount() {
        return parallelizeCount;
    }

    public void setParallelizeCount(int parallelizeCount) {
        this.parallelizeCount = parallelizeCount;
    }

    public Boolean getOutput() {
        return output;
    }

    public void setOutput(Boolean output) {
        this.output = output;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public int getProcessorAmount() {
        return processorAmount;
    }

    public void setProcessorAmount(int processorAmount) {
        this.processorAmount = processorAmount;
    }
}
