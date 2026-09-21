package calburn;

import static org.junit.Assert.*;

// -------------------------------------------------------------------------
/**
 * To test the exercise class.
 * 
 * @author mlkuhl
 * @version Sep 20, 2026
 */
public class ExerciseTest
    extends student.TestCase
{
    private Exercise exercise;
    private Exercise exercise1;

// ----------------------------------------------------------
    /**
     * Set up the constrctor.
     */
    public void setUp()
    {
        exercise = new Exercise("Running", 8.3);
        exercise1 = new Exercise("Rowing", 11.5);
    }


    // ----------------------------------------------------------
    /**
     * Tests the getName() method.
     */
    public void testGetName()
    {
        assertEquals(exercise.getName(), "Running");
        assertFalse(exercise1.getName().equals("Running"));
    }


// ----------------------------------------------------------
    /**
     * Test the getMETValue() method.
     */
    public void testGetMETValue()
    {
        assertEquals(8.3, exercise.getMETValue(), 0.0001);
        assertNotEquals(10.7, exercise1.getMETValue());
    }
}
