package calburn;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

// -------------------------------------------------------------------------
/**
 * Test class for User
 *
 * @author taiyol
 * @version Sep 19, 2026
 */
public class UserTest
    extends student.TestCase
{

    private User user;
    private Exercise running;

    /**
     * Set up for testing User class
     */
    public void setUp()
    {
        user = new User("taiyo", 70);
        running = new Exercise("Running", 10.0);
    }


    /**
     * Tests the constructor
     */
    public void testConstructorValid()
    {
        User u = new User("mikey", 80);
        assertEquals("mikey", u.getID());
        assertEquals(80.0, u.getWeight(), 0.001);
        assertEquals(0.0, u.getWeeklyGoal(), 0.001);
        assertNotNull(u.getSessionLog());
        assertEquals(0, u.getSessionLog().size());

        Exception exception = null;
        try
        {
            new User("bad", 0);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);

        exception = null;
        try
        {
            new User("bad", 401);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);

        exception = null;
        try
        {
            new User(null, 70);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);

        exception = null;
        try
        {
            new User("   ", 70);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }


    /**
     * Tests getID method
     */
    public void testGetId()
    {
        assertEquals("taiyo", user.getID());
    }


    /**
     * Tests setID method
     */
    public void testSetID()
    {
        user.setID("newName");
        assertEquals("newName", user.getID());

        Exception exception = null;
        try
        {
            user.setID(null);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("newName", user.getID());

        exception = null;
        try
        {
            user.setID("   ");
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("newName", user.getID());
    }


    /**
     * Tests getWeight method
     */
    public void testGetWeight()
    {
        assertEquals(70, user.getWeight(), 0.1);
    }


    /**
     * Tests setWeight method
     */
    public void testSetWeight()
    {
        user.setWeight(85.5);
        assertEquals(85.5, user.getWeight(), 0.001);

        Exception exception = null;
        try
        {
            user.setWeight(0);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(85.5, user.getWeight(), 0.001);

        exception = null;
        try
        {
            user.setWeight(-10);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);

        exception = null;
        try
        {
            user.setWeight(500);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }


    /**
     * Tests getSessionLog method
     */
    public void testGetSessionLog()
    {
        assertEquals(0, user.getSessionLog().size());

        Session s = new Session(running, 30, user.getWeight());
        user.getSessionLog().addSession(s);

        assertEquals(1, user.getSessionLog().size());
    }


    /**
     * Tests getWeeklyGoal and setWeeklyGoal
     */
    public void testSetWeeklyGoalValid()
    {
        user.setWeeklyGoal(3500);
        assertEquals(3500.0, user.getWeeklyGoal(), 0.001);

        user.setWeeklyGoal(1000);

        Exception exception = null;
        try
        {
            user.setWeeklyGoal(-1);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(1000.0, user.getWeeklyGoal(), 0.001);
    }


    /**
     * Tests that saving a SessionLog and loading it back into a fresh User
     * reproduces the same sessions
     */
    public void testSaveAndLoadSessionLogRoundTrip()
        throws IOException
    {
        user.getSessionLog().addSession(
            new Session(
                running,
                30,
                70,
                Instant.parse("2026-09-19T09:00:00Z")));
        user.getSessionLog().addSession(
            new Session(
                running,
                45,
                70,
                Instant.parse("2026-09-20T09:00:00Z")));

        File temp = File.createTempFile("sessionlog", ".txt");
        temp.deleteOnExit();

        user.saveSessionLog(temp.getAbsolutePath());

        User loadedUser = new User("reload", 70);
        loadedUser.loadSessionLog(temp.getAbsolutePath());

        assertEquals(2, loadedUser.getSessionLog().size());
        assertEquals(
            user.getSessionLog().getTotal(),
            loadedUser.getSessionLog().getTotal(),
            0.001);

        temp.delete();
    }


    /**
     * Tests that loading from a missing file throws IOException and leaves the
     * existing SessionLog completely unchanged.
     */
    public void testLoadSessionLogMissingFile()
    {
        user.getSessionLog()
            .addSession(new Session(running, 30, user.getWeight()));

        Exception exception = null;
        try
        {
            user.loadSessionLog("/no/such/path/does-not-exist.txt");
        }
        catch (IOException e)
        {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(1, user.getSessionLog().size());
    }
}
