package data;

public interface State {

    /**
     * @return whether visualisation is require
     */
    public Boolean getVisualise();

    /**
     * set whether visualisation is require
     * @param visualise
     */
    public void setVisualise(Boolean visualise);

    /**
     * @return whether parallelization is require
     */
    public Boolean getParallelize();

    /**
     * set whether parallelization is require
     * @param parallelize
     */
    public void setParallelize(Boolean parallelize);

    /**
     * @return the number of require parallelization
     */
    public int getParallelizeCount();

    /**
     * set the number of require parallelization
     * @param parallelizeCount
     */
    public void setParallelizeCount(int parallelizeCount);

    /**
     * @return whether output is require or not
     */
    public Boolean getOutput();

    /**
     * set whether output is require or not
     * @param output
     */
    public void setOutput(Boolean output);

    /**
     * @return the output filename
     */
    public String getOutputFileName();

    /**
     * Set output filename
     * @param outputFileName
     */
    public void setOutputFileName(String outputFileName);

    /**
     * @return input filename
     */
    public String getInputFile();

    /**
     * Set input filename
     * @param inputFile
     */
    public void setInputFile(String inputFile);

    /**
     * @return number of processors
     */
    public int getProcessorAmount();

    /**
     * Set the number of processors
     * @param processorAmount
     */
    public void setProcessorAmount(int processorAmount);
}
