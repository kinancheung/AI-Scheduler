package io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import data.State;
import exception.InvalidInputException;

/**
 * Author: Alex Nicholson
 * Date: 08/08/2021
 *
 * This is a test class for InputParser class which tests the methods within that class.
 */

public class InputParserTest {

    State applicationState;
    InputParser inputParser;

    String inputFile = "test.dot";
    String processorAmount = "2";

    @Before
    public void setup() {
        applicationState = new MockState();
        inputParser = new InputParser(applicationState);
    }

    /**
     * Test that when no arguments are passed the input parser it throws an
     * InvalidInputException with the message "Not enough arguments"
     */
    @Test
    public void testNoArgs() {
        try {
            String[] args = {};

            inputParser.parse(args);
            assert (false);
        } catch (InvalidInputException e) {
            assertEquals("Not enough arguments", e.getMessage());
        }
    }

    /**
     * Test that when one argument is passed to the application the input parser it
     * throwsan InvalidInputException with the message "Not enough arguments"
     */
    @Test
    public void testOneArg() {
        try {
            String[] args = { inputFile };

            inputParser.parse(args);

            assert (false);
        } catch (InvalidInputException e) {
            assertEquals("Not enough arguments", e.getMessage());
        }
    }

    /**
     * Test that when a file name is passed as the first argument, it is set in the
     * application state
     */
    @Test
    public void testInputFileParsedCorrectly() {
        try {
            String[] args = { inputFile, processorAmount };

            inputParser.parse(args);

            assertEquals(applicationState.getInputFile(), inputFile);
        } catch (InvalidInputException e) {
            assert (false);
        }
    }

    /**
     * Test that when a number of processors is passed as the second argument, and
     * it is not a valid integer, it throws an InvalidInputException with the
     * message "The provided processor amount is not a valid number"
     */
    @Test
    public void testInvalidProcessorAmmount() {
        try {
            String inputFile = "test.dot";
            String processorAmount = "notAnInt";
            String[] args = { inputFile, processorAmount };

            inputParser.parse(args);

            assert (false);
        } catch (InvalidInputException e) {
            assertEquals("The provided processor amount is not a valid number", e.getMessage());
        }
    }

    /**
     * Test that when a valid processor amount is passed as the second argument, the
     * application state's processor amount is set to the correct value
     */
    @Test
    public void testValidProcessorAmmount() {
        try {
            String inputFile = "test.dot";
            String processorAmount = "4";
            String[] args = { inputFile, processorAmount };

            inputParser.parse(args);

            assertEquals(applicationState.getProcessorAmount(), 4);
        } catch (InvalidInputException e) {
            assert (false);
        }
    }

    /**
     * Test that when the -v flag is passed to the application, the state correctly
     * has visualisation set to true.
     */
    @Test
    public void testVisualisationSetCorrectly() {
        try {
            String[] args = { inputFile, processorAmount, "-v" };

            inputParser.parse(args);

            assertTrue(applicationState.getVisualise());
        } catch (InvalidInputException e) {
            assert (false);
        }
    }

    /**
     * Test that when the -p flag is passed to the application without being
     * followed by another argument an InvalidInputException is thrown with the
     * message "Please specify a number of processors"
     */
    @Test
    public void testProcessorAmountNotSpecified() {
        try {
            String[] args = { inputFile, processorAmount, "-p" };

            inputParser.parse(args);

            assert (false);
        } catch (InvalidInputException e) {
            assertEquals("Please specify a number of processors", e.getMessage());
        }
    }

    /**
     * Test that when the -p flag is passed to the application with an invalid
     * number of cpus an InvalidInputException is thrown with the message "The
     * provided number of cpus is not valid"
     */
    @Test
    public void testInvalidProcessorAmount() {
        try {
            String cpuAmount = "notAnInt";
            String[] args = { inputFile, processorAmount, "-p", cpuAmount };

            inputParser.parse(args);

            assert (false);
        } catch (InvalidInputException e) {
            assertEquals("The provided number of cpus is not valid", e.getMessage());
        }

    }

    /**
     * Test that when the -p flag is passed to the application with a number of cpus
     * that exceeds the available number of cpus, an InvalidInputException is thrown
     * with the message "The provided number of cpus is not valid"
     */
    @Test
    public void testInvalidProcessorAmountTooHigh() {
        try {
            int maxCpus = Runtime.getRuntime().availableProcessors();
            String cpuAmount = String.valueOf(maxCpus + 1);

            String[] args = { inputFile, processorAmount, "-p", cpuAmount };

            inputParser.parse(args);

            assert (false);
        } catch (InvalidInputException e) {
            assertEquals("The provided number of cpus is not valid",
                    e.getMessage());
        }
    }
}
