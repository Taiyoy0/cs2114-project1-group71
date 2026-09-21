package calburn;

import java.io.IOException;

// -------------------------------------------------------------------------
/**
 * A person’s profile
 *
 * @author taiyol
 * @version Sep 19, 2026
 */
public class User
{
    // ~ Fields ................................................................
    private static final double MIN_WEIGHT_KG = 0;
    private static final double MAX_WEIGHT_KG = 400;

    private String id;
    private double weightKg;
    private SessionLog sessionLog;
    private double weeklyGoal;

    // ~ Constructors ..........................................................

    /**
     * Constructs a new User. Defaults weeklyGoal to 0 and starts with an empty
     * SessionLog.
     *
     * @param id
     *            the user's identifier
     * @param weightKg
     *            the user's body weight in kilograms
     * @throws IllegalArgumentException
     *             if id is null/empty, or weightKg is <= 0 or > 400
     */
    public User(String id, double weightKg)
        throws IllegalArgumentException
    {
        setID(id);
        setWeight(weightKg);
        sessionLog = new SessionLog();
        weeklyGoal = 0;
    }

    // ~ Public Methods ........................................................


    /**
     * Returns the user's identifier.
     *
     * @return the user id
     */
    public String getID()
    {
        return id;
    }


    /**
     * Sets the user's identifier.
     *
     * @param newID
     *            the new id
     * @throws IllegalArgumentException
     *             if newID is null or empty (after trimming whitespace)
     */
    public void setID(String newID)
        throws IllegalArgumentException
    {
        if (newID == null || newID.trim().isEmpty())
        {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        id = newID;
    }


    /**
     * Returns the user's body weight.
     *
     * @return weight in kilograms
     */
    public double getWeight()
    {
        return weightKg;
    }


    /**
     * Sets the user's body weight.
     *
     * @param newWeight
     *            the new weight in kilograms
     * @throws IllegalArgumentException
     *             if newWeight is <= 0 or > 400
     */
    public void setWeight(double newWeight)
        throws IllegalArgumentException
    {
        if (newWeight <= MIN_WEIGHT_KG || newWeight > MAX_WEIGHT_KG)
        {
            throw new IllegalArgumentException(
                "Weight must be greater than 0 and at most " + MAX_WEIGHT_KG
                    + " kg");
        }
        weightKg = newWeight;
    }


    /**
     * Returns this user's session history.
     *
     * @return the user's SessionLog
     */
    public SessionLog getSessionLog()
    {
        return sessionLog;
    }


    /**
     * Returns the user's weekly calorie goal.
     *
     * @return the weekly goal, in calories; 0 means no goal is set
     */
    public double getWeeklyGoal()
    {
        return weeklyGoal;
    }


    /**
     * Sets the user's weekly calorie goal.
     *
     * @param newGoal
     *            the new weekly goal, in calories
     * @throws IllegalArgumentException
     *             if newGoal is negative
     */
    public void setWeeklyGoal(double newGoal)
        throws IllegalArgumentException
    {
        if (newGoal < 0)
        {
            throw new IllegalArgumentException(
                "Weekly goal cannot be negative");
        }
        weeklyGoal = newGoal;
    }


    /**
     * Populates this user's SessionLog from a text file previously written by
     * saveSessionLog. On failure, the existing sessionLog is left completely
     * unchanged
     *
     * @param path
     *            the file to read from
     * @throws IOException
     *             if the file can't be read, or its contents are malformed
     */
    public void loadSessionLog(String path)
        throws IOException
    {
        sessionLog.loadSessionLog(path);
    }


    /**
     * Writes this user's SessionLog out to a text file.
     *
     * @param path
     *            the file to write to
     * @throws IOException
     *             if the file can't be created or written to
     */
    public void saveSessionLog(String path)
        throws IOException
    {
        sessionLog.saveSessionLog(path);
    }
}
