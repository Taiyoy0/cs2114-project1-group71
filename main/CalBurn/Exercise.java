package CalBurn;

// -------------------------------------------------------------------------
/**
 * Record of one exercise's name and MET Value.
 * 
 * @author mlkuhl
 * @version Sep 17, 2026
 */
public class Exercise
{
    private String name;
    private double MET_Value;

    // ----------------------------------------------------------
    /**
     * Create a new Exercise object.
     * 
     * @param name
     *            - name of the exercise.
     * @param MET_Value
     *            - MET Value for the exercise.
     */
    public Exercise(String name, double MET_Value)
    {
        this.name = name;
        this.MET_Value = MET_Value;
    }


    // ----------------------------------------------------------
    /**
     * Getter method for the name of the exercise.
     * 
     * @return the name of the exercise.
     */
    public String getName()
    {
        return this.name;
    }


    // ----------------------------------------------------------
    /**
     * Getter method for the name of the exercise.
     * 
     * @return MET Value of the exercise.
     */
    public double getMETValue()
    {
        return this.MET_Value;
    }
}
