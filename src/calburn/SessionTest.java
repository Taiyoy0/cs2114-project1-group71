package calburn;

import java.time.Instant;

// -------------------------------------------------------------------------
/**
 * Test class for Session
 * 
 * @author taiyol
 * @version Sep 19, 2026
 */
public class SessionTest
    extends student.TestCase
{

    private Exercise running;
    private Session session1;
    private Session session2;

    /**
     * Set up for testing Session class
     */
    public void setUp()
    {
        running = new Exercise("Running", 12.5);
        session1 =
            new Session(running, 15, 60, Instant.parse("2026-09-19T15:30:00Z"));
        session2 = new Session(running, 15, 60);
    }


    /**
     * Tests Session constructor and it's exception throwing
     */
    public void testSession()
    {
        Exception exception = new Exception();
        try
        {
            session1 = new Session(running, 0, 60, Instant.now());
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(
            "Duration cannot be equal to or less than 0 minutes",
            exception.getMessage());

        exception = null;
        try
        {
            session1 = new Session(running, -5, 60, Instant.now());
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(
            "Duration cannot be equal to or less than 0 minutes",
            exception.getMessage());

        exception = null;
        try
        {
            session1 = new Session(running, 1441, 60, Instant.now());
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(
            "Duration cannot be more than 24 hours (1440 minutes)",
            exception.getMessage());
    }


    /**
     * Tests getExercise method
     */
    public void testGetExercise()
    {
        assertEquals(running, session1.getExercise());
    }


    /**
     * Tests getDuration method
     */
    public void testGetDuration()
    {
        assertEquals(15, session1.getDuration());
    }


    /**
     * Tests getWeightKg method
     */
    public void testGetWeightKg()
    {
        assertEquals(60, session1.getWeightKg(), 0.1);
    }


    /**
     * Tests getDate method
     */
    public void testGetDate()
    {
        assertTrue(
            Instant.parse("2026-09-19T15:30:00Z").equals(session1.getDate()));

        assertNotNull(session2.getDate());
    }


    /**
     * Tests getCaloriesBurned method
     */
    public void testGetCaloriesBurned()
    {
        assertEquals(196.875, session1.getCaloriesBurned(), 0.001);
    }
}
