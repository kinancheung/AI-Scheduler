package util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import visualisation.util.ChartUtils;

public class ChartUtilsTest {

    /**
     * Test that when passed an input of 14 the output is 20
     */
    @Test
    public void testCalculateChartWidth() {
        assertEquals(20, ChartUtils.getNearestWidth(14));
    }

    /**
     * Test that when passed an input of 10 the output is 10
     */
    @Test
    public void testCalculateChartWidth2() {
        assertEquals(10, ChartUtils.getNearestWidth(10));
    }

    /**
     * Test that when passed an input of 1 the output is 10
     */
    @Test
    public void testCalculateChartWidth3() {
        assertEquals(1, ChartUtils.getNearestWidth(1));
    }

    /**
     * Test that when passed an input of 68 the output is 70
     */
    @Test
    public void testCalculateChartWidth4() {
        assertEquals(70, ChartUtils.getNearestWidth(68));
    }

    /**
     * Test that when passed an input of 16782 the output is 17000
     */
    @Test
    public void testCalculateChartWidth5() {
        assertEquals(20000, ChartUtils.getNearestWidth(16782));
    }

}
