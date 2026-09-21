package calburn;

// -------------------------------------------------------------------------
/**
 * Tests the ExerciseList class.
 * 
 * @author mlkuhl
 * @version Sep 20, 2026
 */
public class ExerciseListTest
    extends student.TestCase
{
    private String testPath = "exercises.csv";
    private ExerciseList list;

    public void setUp()
    {
        list = new ExerciseList();
        list.loadFromFile(testPath);
    }


    // ----------------------------------------------------------
    /**
     * Make sure the count of exercises is correct.
     */
    public void testExerciseCount()
    {
        assertEquals(8, list.listExercises().length);
    }


    // ----------------------------------------------------------
    /**
     * Test getExercise()
     */
    public void testGetExercise()
    {
        Exercise ex = list.getExercise("Running");
        assertEquals(8.3, ex.getMETValue(), 0.0001);
    }


    // ----------------------------------------------------------
    /**
     * Test that null is returned for exercises that don't exist.
     */
    public void testGetExerciseNull()
    {
        assertNull(list.getExercise("Sitting in bed"));
    }
}
