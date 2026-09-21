package calburn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 * A collection of a user’s Sessions
 *
 * @author taiyol
 * @version Sep 19, 2026
 */
public class SessionLog {
    // ~ Fields ................................................................
    private ArrayList<Session> sessions;

    // ~ Constructors ..........................................................

    /**
     * Constructs an empty SessionLog.
     */
    public SessionLog() {
        sessions = new ArrayList<Session>();
    }

    // ~ Public Methods ........................................................


    /**
     * Appends a session to the log.
     *
     * @param session
     *            the session to add
     * @return false without adding if session is null, otherwise true
     */
    public boolean addSession(Session session) {
        if (session == null) {
            return false;
        }
        sessions.add(session);
        return true;
    }


    /**
     * Returns how many sessions are currently in the log.
     *
     * @return the number of logged sessions
     */
    public int size() {
        return sessions.size();
    }


    /**
     * Sums caloriesBurned across every session ever logged.
     *
     * @return total calories burned across all sessions
     */
    public double getTotal() {
        double total = 0;
        for (Session s : sessions) {
            total += s.getCaloriesBurned();
        }
        return total;
    }


    /**
     * Sums caloriesBurned for sessions with time in [start, end).
     *
     * @param start
     *            inclusive start of the range
     * @param end
     *            exclusive end of the range
     * @return total calories burned within the range; 0 if start or end is
     *         null, or if start is not strictly before end
     */
    public double getTotal(Instant start, Instant end) {
        if (start == null || end == null || !start.isBefore(end)) {
            return 0;
        }

        double total = 0;
        for (Session s : sessions) {
            Instant time = s.getDate();
            if (!time.isBefore(start) && time.isBefore(end)) {
                total += s.getCaloriesBurned();
            }
        }
        return total;
    }


    /**
     * Calls getTotal for a specific day from midnight to midnight
     *
     * @param day
     *            any instant within the day of interest
     * @return total calories burned that day
     */
    public double getDailyTotal(Instant day) {
        Instant start = day.truncatedTo(ChronoUnit.DAYS);
        Instant end = start.plus(1, ChronoUnit.DAYS);
        return getTotal(start, end);
    }


    /**
     * Calls getTotal from weekStart to 7 days later
     *
     * @param weekStart
     *            the start of the week
     * @return total calories burned that week
     */
    public double getWeeklyTotal(Instant weekStart) {
        Instant start = weekStart.truncatedTo(ChronoUnit.DAYS);
        Instant end = start.plus(7, ChronoUnit.DAYS);
        return getTotal(start, end);
    }


    /**
     * Calls getTotal from the start of a given month to end of month
     *
     * @param year
     *            the year, e.g. 2026
     * @param month
     *            the month, 1-12
     * @return total calories burned that month
     * @throws IllegalArgumentException
     *             if month is not between 1 and 12
     */
    public double getMonthlyTotal(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(
                "Month must be between 1 and 12");
        }

        YearMonth ym = YearMonth.of(year, month);
        Instant start = ym.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = ym.atEndOfMonth().plusDays(1).atStartOfDay(ZoneOffset.UTC)
            .toInstant();
        return getTotal(start, end);
    }


    /**
     * Writes this log out to a text file, one session per line, in the
     * format exerciseName,metValue,durationMin,weightKg,epochMilli
     *
     * @param path
     *            the file to write to
     *
     * @throws IOException
     *             if the file can't be created or written to
     */
    public void saveSessionLog(String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Session s : sessions) {
                writer.write(formatSessionLine(s));
                writer.newLine();
            }
        }
    }


    /**
     * Populates this log from a text file previously written by saveSessionLog.
     * If any line is malformed, an IOException is thrown and the
     * log's existing contents are left completely unchanged.
     *
     * @param path
     *            the file to read from
     *
     * @throws IOException
     *             if the file can't be read, or its contents are malformed
     */
    public void loadSessionLog(String path) throws IOException {
        ArrayList<Session> loaded = new ArrayList<Session>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                loaded.add(parseSessionLine(line));
            }
        }
        catch (RuntimeException e) {
            throw new IOException("Malformed session log file: " + path, e);
        }

        sessions = loaded;
    }

    // ~ Private Helper Methods ................................................


    /**
     * Formats a single Session as one CSV line for saveSessionLog.
     */
    private String formatSessionLine(Session s) {
        Exercise ex = s.getExercise();
        return ex.getName() + "," + ex.getMETValue() + "," + s.getDuration()
            + "," + s.getWeightKg() + "," + s.getDate().toEpochMilli();
    }


    /**
     * Parses one CSV line from loadSessionLog back into a Session.
     *
     * @throws RuntimeException
     *             (NumberFormatException, IllegalArgumentException, etc.)
     *             if the line is malformed; caught and wrapped by
     *             loadSessionLog
     */
    private Session parseSessionLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 5) {
            throw new IllegalArgumentException("Expected 5 fields, found "
                + parts.length + ": " + line);
        }

        String exerciseName = parts[0];
        double metValue = Double.parseDouble(parts[1]);
        int durationMin = Integer.parseInt(parts[2]);
        double weightKg = Double.parseDouble(parts[3]);
        Instant time = Instant.ofEpochMilli(Long.parseLong(parts[4]));

        Exercise exercise = new Exercise(exerciseName, metValue);
        return new Session(exercise, durationMin, weightKg, time);
    }
}
