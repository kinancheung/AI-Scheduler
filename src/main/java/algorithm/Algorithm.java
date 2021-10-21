package algorithm;

import data.Schedule;
/*
    Author: Kinan,Peter
    Date: 18/08/21

    Algorithm is an interface which ensures BranchAndBoundRoutine must be implemented
 */

public interface Algorithm {

    /**
     * The main routine for the Branch and Bound Parallel algorithm. A thread pool is created using the
     * Form and Join method which calls the recursive class BranchAndBoundRecursive and invokes the compute method
     * @return optimal schedule
     */
    public Schedule BranchAndBoundRoutine();

}
