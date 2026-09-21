package calburn;

import java.io.*;
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
    private HashMap<String, Exercise> exercises = new HashMap<>();

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
        return exercises.get(name);
    }


    // ----------------------------------------------------------
    /**
     * Returns all valid exercises in the HashMap.
     * 
     * @return Exercise[] array of all exercises in the HashMap.
     */
    public Exercise[] listExercises()
    {
        return exercises.values().toArray(new Exercise[0]);
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
        try (BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null)
            {
                if (firstLine)
                {
                    firstLine = false;
                    continue;
                }
                if (line.trim().isEmpty())
                {
                    continue;
                }
                String[] fields = line.split(",");
                if (fields.length < 2)
                {
                    continue;
                }
                String name = fields[0].trim();
                double metValue;
                try
                {
                    metValue = Double.parseDouble(fields[1].trim());
                }
                catch (NumberFormatException n)
                {
                    continue;
                }
                exercises.put(name, new Exercise(name, metValue));
            }
        }
        catch (IOException i)
        {
            throw new RuntimeException(
                "Couldn't load exercises from " + path,
                i);
        }
    }
}
