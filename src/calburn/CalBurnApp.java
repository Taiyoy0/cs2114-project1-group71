package calburn;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

// -------------------------------------------------------------------------
/**
 * Driver Class for the app.
 *
 * @author mlkuhl
 * @version Sep 17, 2026
 */
public class CalBurnApp
{
    private ExerciseList exerciseList;
    private User currentUser;
    private Scanner scanner;

    // ----------------------------------------------------------
    /**
     * Constructs the app, loading the exercise table and preparing
     * console input.
     */
    public CalBurnApp()
    {
        exerciseList = new ExerciseList();
        exerciseList.loadFromFile("exercisedatabase.csv");
        scanner = new Scanner(System.in);
    }


    // ----------------------------------------------------------
    /**
     * Main method, the driver for the Calburn App.
     *
     * @param args
     *            - Won't be utilized.
     */
    public static void main(String[] args)
    {
        CalBurnApp app = new CalBurnApp();
        app.run();
    }


    // ----------------------------------------------------------
    /**
     * Runs the interactive menu loop until the user chooses to quit.
     */
    public void run()
    {
        createUser();

        boolean running = true;
        while (running)
        {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice)
            {
                case "1":
                    logSession();
                    break;
                case "2":
                    listExercises();
                    break;
                case "3":
                    viewTotals();
                    break;
                case "4":
                    setWeeklyGoal();
                    break;
                case "5":
                    saveSessionLog();
                    break;
                case "6":
                    loadSessionLog();
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
        System.out.println("Goodbye.");
    }


    // ----------------------------------------------------------
    /**
     * Prompts for a user id and weight, retrying on invalid input,
     * until a valid User is created.
     */
    private void createUser()
    {
        while (currentUser == null)
        {
            System.out.print("Enter user ID: ");
            String id = scanner.nextLine().trim();

            System.out.print("Enter your weight in kg: ");
            String weightInput = scanner.nextLine().trim();

            try
            {
                double weight = Double.parseDouble(weightInput);
                currentUser = new User(id, weight);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Weight must be a number. Try again.");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Prints the main menu options.
     */
    private void printMenu()
    {
        System.out.println();
        System.out.println("1. Log a session");
        System.out.println("2. List available exercises");
        System.out.println("3. View calorie totals");
        System.out.println("4. Set weekly goal");
        System.out.println("5. Save session log");
        System.out.println("6. Load session log");
        System.out.println("7. Quit");
        System.out.print("Choose an option: ");
    }


    // ----------------------------------------------------------
    /**
     * Prompts for an exercise name and duration, then logs a new
     * session for the current user using their stored body weight.
     */
    private void logSession()
    {
        System.out.print("Enter exercise name: ");
        String name = scanner.nextLine().trim();
        Exercise exercise = exerciseList.getExercise(name);
        if (exercise == null)
        {
            System.out.println("No exercise found with that name.");
            return;
        }

        System.out.print("Enter duration in minutes: ");
        String durationInput = scanner.nextLine().trim();

        try
        {
            int duration = Integer.parseInt(durationInput);
            Session session = new Session(
                exercise, duration, currentUser.getWeight());
            currentUser.getSessionLog().addSession(session);
            System.out.printf(
                "Logged. Calories burned: %.2f%n",
                session.getCaloriesBurned());
        }
        catch (NumberFormatException e)
        {
            System.out.println("Duration must be a whole number.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }


    // ----------------------------------------------------------
    /**
     * Prints every exercise currently loaded, with its MET value.
     */
    private void listExercises()
    {
        Exercise[] all = exerciseList.listExercises();
        if (all.length == 0)
        {
            System.out.println("No exercises loaded.");
            return;
        }
        for (Exercise ex : all)
        {
            System.out.printf(
                "%s (MET %.1f)%n", ex.getName(), ex.getMETValue());
        }
    }


    // ----------------------------------------------------------
    /**
     * Prints today's, this week's, this month's, and all-time
     * calorie totals for the current user.
     */
    private void viewTotals()
    {
        SessionLog log = currentUser.getSessionLog();
        Instant now = Instant.now();

        double daily = log.getDailyTotal(now);

        Instant weekStart =
            now.truncatedTo(ChronoUnit.DAYS).minus(6, ChronoUnit.DAYS);
        double weekly = log.getWeeklyTotal(weekStart);

        ZonedDateTime zdt = now.atZone(ZoneOffset.UTC);
        double monthly = log.getMonthlyTotal(zdt.getYear(), zdt.getMonthValue());

        double allTime = log.getTotal();

        System.out.printf("Today: %.2f calories%n", daily);
        System.out.printf("Last 7 days: %.2f calories%n", weekly);
        System.out.printf("This month: %.2f calories%n", monthly);
        System.out.printf("All time: %.2f calories%n", allTime);

        if (currentUser.getWeeklyGoal() > 0)
        {
            double remaining = currentUser.getWeeklyGoal() - weekly;
            if (remaining > 0)
            {
                System.out.printf(
                    "%.2f calories left to reach your weekly goal.%n",
                    remaining);
            }
            else
            {
                System.out.println("Weekly goal reached.");
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Prompts for and sets a new weekly calorie goal.
     */
    private void setWeeklyGoal()
    {
        System.out.print("Enter new weekly calorie goal: ");
        String input = scanner.nextLine().trim();
        try
        {
            currentUser.setWeeklyGoal(Double.parseDouble(input));
            System.out.println("Weekly goal updated.");
        }
        catch (NumberFormatException e)
        {
            System.out.println("Goal must be a number.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }


    // ----------------------------------------------------------
    /**
     * Prompts for a path and saves the current user's session log
     * to it.
     */
    private void saveSessionLog()
    {
        System.out.print("Enter file path to save to: ");
        String path = scanner.nextLine().trim();
        try
        {
            currentUser.saveSessionLog(path);
            System.out.println("Session log saved.");
        }
        catch (IOException e)
        {
            System.out.println("Failed to save: " + e.getMessage());
        }
    }


    // ----------------------------------------------------------
    /**
     * Prompts for a path and loads a session log from it, replacing
     * the current user's session history.
     */
    private void loadSessionLog()
    {
        System.out.print("Enter file path to load from: ");
        String path = scanner.nextLine().trim();
        try
        {
            currentUser.loadSessionLog(path);
            System.out.println("Session log loaded.");
        }
        catch (IOException e)
        {
            System.out.println("Failed to load: " + e.getMessage());
        }
    }
}