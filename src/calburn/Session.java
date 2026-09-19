package calburn;

import java.time.Instant;

// -------------------------------------------------------------------------
/**
 * A single logged session of an exercise
 * 
 * @author taiyol
 * @version Sep 16, 2026
 */
public class Session {
    // ~ Fields ................................................................
    private Exercise exercise;
    private int durationMin;
    private Instant time;
    private double caloriesBurned;

    // ~ Constructors ..........................................................

    /**
     * Constructs a Session object. Calculates caloriesBurned from given values.
     * 
     * @param exercise
     *            The type of exercise
     * @param durationMin
     *            the duration the exercise was done for
     * @param weightKg
     *            the weight of the individual during the activity
     * @param time
     *            the time the exercise was done
     * 
     * @throws IllegalArgumentException
     *             if the duration is <= 0 or > 1440 minutes
     */
    public Session(
        Exercise exercise,
        int durationMin,
        double weightKg,
        Instant time)
        throws IllegalArgumentException {
        if (durationMin <= 0) {
            throw new IllegalArgumentException(
                "Duration cannot be equal to or less than 0 minutes");
        }
        if (durationMin > 1440) {
            throw new IllegalArgumentException(
                "Duration cannot be more than 24 hours (1440 minutes)");
        }

        this.exercise = exercise;
        this.durationMin = durationMin;
        this.time = time;

        caloriesBurned = (exercise.getMETValue() * 3.5 * weightKg / 200)
            * durationMin;
    }


    /**
     * Constructs a Session object. Calculates caloriesBurned from given values.
     * defaults time to Instant.now()
     * 
     * @param exercise
     *            The type of exercise
     * @param durationMin
     *            the duration the exercise was done for
     * @param weightKg
     *            the weight of the individual during the activity
     * 
     * @throws IllegalArgumentException
     *             if the duration is <= 0 or > 1440 minutes
     */
    public Session(Exercise exercise, int durationMin, double weightKg)
        throws IllegalArgumentException {
        this(exercise, durationMin, weightKg, Instant.now());
    }


    /**
     * Returns the exercise name
     * 
     * @return The exercise done
     */
    public Exercise getExercise() {
        return exercise;
    }


    /**
     * Returns the duration of the session in minutes
     * 
     * @return the duration of exercise
     */
    public int getDuration() {
        return durationMin;
    }


    /**
     * Returns the time the exercise was completed
     * 
     * @return the time in ISO-8601 format, UTC
     */
    public Instant getDate() {
        return time;
    }


    /**
     * Returns the calculated number of calories burned during the session
     * 
     * @return theoretical calories burned
     */
    public double getCaloriesBurned() {
        return caloriesBurned;
    }

}
