package calburn;

import java.util.HashMap;

// -------------------------------------------------------------------------
/**
 * Fixed lookup table of all valid Exercises
 * 
 * @author mlkuhl
 * @version Sep 17, 2026
 */
public class ExerciseList
{
    private HashMap<String, Exercise> exercises;

    // ----------------------------------------------------------
    /**
     * Returns the Exercise object that corresponds with the given name from the
     * HashMap.
     * 
     * @param name
     *            - name of object to search for.
     * @return Exercise object with the given name.
     */
    public Exercise getExercise(String name)
    {
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Returns all valid exercises in the HashMap.
     * 
     * @return Exercise[] array of all exercises in the HashMap.
     */
    public Exercise[] listExercises()
    {
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Loads exercises from a csv file into the HashMap.
     * 
     * @param path
     *            - path of csv file to load exercises from and into the
     *            hashmap.
     */
    public void loadFromFile(String path)
    {
        
    }
}
