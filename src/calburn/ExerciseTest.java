package calburn;

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

// ----------------------------------------------------------
    /**
     * Set up the constrctor.
     */
    public void setUp()
    {
        exercise = new Exercise("Running", 8.3);
    }


    // ----------------------------------------------------------
    /**
     * Tests the getName() method.
     */
    public void testGetName()
    {
        assertEquals(exercise.getName(), "Running");
    }


// ----------------------------------------------------------
    /**
     * Test the getMETValue() method.
     */
    public void testGetMETValue()
    {
        assertEquals(8.3, exercise.getMETValue(), 0.0001);
    }
}
