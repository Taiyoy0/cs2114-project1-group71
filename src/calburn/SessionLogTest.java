package calburn;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

// -------------------------------------------------------------------------
/**
 * Test class for SessionLog
 *
 * @author taiyol
 * @version Sep 19, 2026
 */
public class SessionLogTest
    extends student.TestCase
{

    private Exercise running;
    private SessionLog log;

    private Session s1;
    private Session s2;
    private Session s3;
    private Session s4;
    private Session s5;

    /**
     * Set up for testing SessionLog class
     */
    public void setUp()
    {
        running = new Exercise("Running", 10.0);

        s1 = new Session(
            running,
            30,
            100,
            Instant.parse("2026-09-15T10:00:00Z"));
        s2 = new Session(
            running,
            40,
            100,
            Instant.parse("2026-09-19T09:00:00Z"));
        s3 = new Session(
            running,
            20,
            100,
            Instant.parse("2026-09-19T20:00:00Z"));
        s4 = new Session(
            running,
            50,
            100,
            Instant.parse("2026-09-20T08:00:00Z"));
        s5 = new Session(
            running,
            25,
            100,
            Instant.parse("2026-08-31T23:59:59Z"));

        log = new SessionLog();
        log.addSession(s1);
        log.addSession(s2);
        log.addSession(s3);
        log.addSession(s4);
        log.addSession(s5);
    }


    /**
     * Tests addSession method
     */
    public void testAddSession()
    {
        SessionLog fresh = new SessionLog();
        assertEquals(0, fresh.size());

        boolean added = fresh.addSession(s1);
        assertTrue(added);
        assertEquals(1, fresh.size());

        boolean addedNull = fresh.addSession(null);
        assertFalse(addedNull);
        assertEquals(1, fresh.size());
    }


    /**
     * Tests size method
     */
    public void testSize()
    {
        SessionLog fresh = new SessionLog();
        assertEquals(0, fresh.size());

        fresh.addSession(s1);
        assertEquals(1, fresh.size());
    }


    /**
     * Tests getTotal method
     */
    public void testGetTotal()
    {

        SessionLog fresh = new SessionLog();
        assertEquals(0.0, fresh.getTotal(), 0.001);

        assertEquals(2887.5, log.getTotal(), 0.001);

        Instant start = Instant.parse("2026-09-19T00:00:00Z");
        Instant end = Instant.parse("2026-09-20T00:00:00Z");
        assertEquals(1050.0, log.getTotal(start, end), 0.001);

        start = Instant.parse("2026-09-20T00:00:00Z");
        end = Instant.parse("2026-09-19T00:00:00Z");
        assertEquals(0.0, log.getTotal(start, end), 0.001);

        Instant same = Instant.parse("2026-09-19T09:00:00Z");
        assertEquals(0.0, log.getTotal(same, same), 0.001);

        assertEquals(0.0, log.getTotal(null, end), 0.001);
        assertEquals(0.0, log.getTotal(start, null), 0.001);
    }


    /**
     * Tests getDailyTotal method
     */
    public void testGetDailyTotal()
    {
        Instant day = Instant.parse("2026-09-19T05:00:00Z");
        assertEquals(1050.0, log.getDailyTotal(day), 0.001);

        day = Instant.parse("2026-09-16T00:00:00Z");
        assertEquals(0.0, log.getDailyTotal(day), 0.001);
    }


    /**
     * Tests getWeeklyTotal method
     */
    public void testGetWeeklyTotal()
    {
        Instant weekStart = Instant.parse("2026-09-14T00:00:00Z");
        assertEquals(2450.0, log.getWeeklyTotal(weekStart), 0.001);

        weekStart = Instant.parse("2026-01-05T00:00:00Z");
        assertEquals(0.0, log.getWeeklyTotal(weekStart), 0.001);
    }


    /**
     * Tests getMonthlyTotal method
     */
    public void testGetMonthlyTotal()
    {
        assertEquals(2450.0, log.getMonthlyTotal(2026, 9), 0.001);

        assertEquals(0.0, log.getMonthlyTotal(2025, 1), 0.001);

        Exception exception = null;
        try
        {
            log.getMonthlyTotal(2026, 13);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);

        exception = null;
        try
        {
            log.getMonthlyTotal(2026, 0);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }


    /**
     * Tests boundary values for getMonthlyTotal
     */
    public void testGetMonthlyTotalBoundary()
    {
        SessionLog boundaryLog = new SessionLog();
        Session firstOfSept = new Session(
            running,
            10,
            100,
            Instant.parse("2026-09-01T00:00:00Z"));
        Session lastOfAugust = new Session(
            running,
            10,
            100,
            Instant.parse("2026-08-31T23:59:59Z"));

        boundaryLog.addSession(firstOfSept);
        boundaryLog.addSession(lastOfAugust);

        assertEquals(175.0, boundaryLog.getMonthlyTotal(2026, 9), 0.001);
    }


    /**
     * Tests that saving a SessionLog and loading it back into a fresh
     * SessionLog reproduces the same sessions (round-trip test)
     */
    public void testSaveAndLoadSessionLogRoundTrip()
        throws IOException
    {
        SessionLog toSave = new SessionLog();
        toSave.addSession(
            new Session(
                running,
                30,
                70,
                Instant.parse("2026-09-19T09:00:00Z")));
        toSave.addSession(
            new Session(
                running,
                45,
                70,
                Instant.parse("2026-09-20T09:00:00Z")));

        File temp = File.createTempFile("sessionlog", ".txt");
        temp.deleteOnExit();

        toSave.saveSessionLog(temp.getAbsolutePath());

        SessionLog loaded = new SessionLog();
        loaded.loadSessionLog(temp.getAbsolutePath());

        assertEquals(2, loaded.size());
        assertEquals(toSave.getTotal(), loaded.getTotal(), 0.001);

        temp.delete();
    }


    /**
     * Tests that loading from a missing file throws IOException and leaves the
     * log's existing contents completely unchanged
     */
    public void testLoadSessionLogMissingFile()
    {
        int sizeBefore = log.size();

        Exception exception = null;
        try
        {
            log.loadSessionLog("/no/such/path/does-not-exist.txt");
        }
        catch (IOException e)
        {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(sizeBefore, log.size());
    }


    /**
     * Tests that loading a corrupt file throws IOException and leaves the log's
     * existing contents completely unchanged
     */
    public void testLoadSessionLogCorruptFile()
        throws IOException
    {
        int sizeBefore = log.size();

        File temp = File.createTempFile("corrupt", ".txt");
        temp.deleteOnExit();
        java.nio.file.Files.write(
            temp.toPath(),
            "this,is,not,a,valid,session,line\n".getBytes());

        Exception exception = null;
        try
        {
            log.loadSessionLog(temp.getAbsolutePath());
        }
        catch (IOException e)
        {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(sizeBefore, log.size());

        temp.delete();
    }


    /**
     * Tests that saving to an invalid path (a directory that doesn't exist)
     * throws IOException
     */
    public void testSaveSessionLogInvalidPath()
    {
        Exception exception = null;
        try
        {
            log.saveSessionLog("/no/such/directory/out.txt");
        }
        catch (IOException e)
        {
            exception = e;
        }

        assertNotNull(exception);
    }
}
