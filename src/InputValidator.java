import java.io.File;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This program:
 *      prompts for and reads the user's first name, then last name
 *                  -- each should be at most 50 characters
 *                  -- decide what is legitimate for characters for first and last name
 *      prompts for and reads in two int values from the user (range of values are those of a 4 byte int)
 *      prompts for and reads the name of an input file from the user
 *      prompts for and reads the name of an output file from the user
 *      prompts the user to enter a password,
 *                          store the password,
 *                          then ask the user to re-enter the password and verify that it is correct
 *      password should be hashed using a salt and written to a file
 *      to validate, grab hash from file and compare it to hash from second user entry for password
 *      opens the output file and
 *          writes the user's name
 *          writes the result of adding the two integer values (no overflow should occur)
 *          writes the result of multiplying the two integer values (no overflow should occur),
 *          writes the contents of the input file
 *
 *      Each thing written should be clearly labeled (e.g. First name,
 *                                                    Last name,
 *                                                    First Integer,
 *                                                    Second Integer,
 *                                                    Sum,
 *                                                    Product,
 *                                                    Input File Name,
 *                                                    Input file contents)
 *
 * NOTE: it is ok to echo output to the screen as you wish
 *
 * @author Dustin Ray
 * @version Summer 2021
 *
 */
public class InputValidator {

    private String myFirstName;
    private String myLastName;
    private int myFirstInt;
    private int mySecondInt;
    private File myInputFile;
    private File myOutputFile;
    private String myPassword;
    private int mySum;
    private int myProduct;
    private final SecureRandom myRandom;

    public InputValidator() {

        myRandom = new SecureRandom();
//        getFirstName();
//        getLastName();
////        getIntOne();
////        getIntTwo();
////        addIntegers();
////        multiplyIntegers();
        getPassword();
    }


    private void getFirstName() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your first name: ");
        String regex = "^([a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð,'-]{2,50})$";
        String input = sc.nextLine();

        while (!checkPattern(input, regex)) {
            System.out.println("Im being really generous with what's allowed here... Please enter a valid first name... ");
            input = sc.nextLine();
        };
        myFirstName = input;
    }

    private void getLastName() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your last name: ");
        String regex = "^([a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð,'-]{2,50})$";
        String input = sc.nextLine();

        while (!checkPattern(input, regex)) {
            System.out.println("You're breaking my heart... Please enter a valid last name... ");
            input = sc.nextLine();
        };
        myLastName = input;

    }

    private void getIntOne() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter an integer: ");

        while (!sc.hasNextInt()) {
            System.out.println("But...I thought we were friends... Please enter a valid integer. ");
        };
        myFirstInt = sc.nextInt();
    }

    public void getIntTwo() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter another integer: ");
        while (!sc.hasNextInt()) {
            System.out.println("Please enter your favorite color- wait I mean INTEGER... Please enter a valid integer...");
        };
        mySecondInt = sc.nextInt();
    }


    public void addIntegers() {
        while ((myFirstInt > 0) && (mySecondInt > Integer.MAX_VALUE - myFirstInt) ||
                ((myFirstInt < 0) && (mySecondInt < Integer.MIN_VALUE - myFirstInt))) {
            System.out.println("Are you trying to spill something?");
            getIntOne();
            getIntTwo();
        }
        mySum = myFirstInt + mySecondInt;
    }

    public void multiplyIntegers() {
        while ((myFirstInt != 0 && mySecondInt != 0) &&
                (((myFirstInt == -1) && (mySecondInt == Integer.MIN_VALUE) ||
                ((mySecondInt == -1) && (myFirstInt == Integer.MIN_VALUE))) ||
                 (myFirstInt > Integer.MAX_VALUE / mySecondInt) ||
                ((myFirstInt < Integer.MIN_VALUE / mySecondInt)))) {
            System.out.println("I feel like you bowl with the gutters up...");
            getIntOne();
            getIntTwo();
        }
        myProduct = myFirstInt * mySecondInt;
    }

    /** 48as4tAa1!48as4tAa1! */
    public void getPassword() {

        String passwordRequirements = """
                Please enter a password that contains at least:
                   * 10 characters
                   * and includes at least:
                   * one upper case character,
                   * lower case character,
                   * digit,
                   * punctuation mark,
                   * and does not have more than 3 consecutive lower case characters:
                """;

        final int salt = myRandom.nextInt(Integer.MAX_VALUE);
        String regex = "^(?=.{10,}$)(?=\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*([a-z])\\1{2}).*$";
        Scanner sc = new Scanner(System.in);
        System.out.println(passwordRequirements);
        String password = sc.nextLine();
        while (!checkPattern(password, regex)) {
            System.out.println(passwordRequirements);
            password = sc.nextLine();
        }

        String saltedPassword = password + salt;
        System.out.println("I'm not echoing your password, that seems unsafe. I guess I can show you your salt though. \n" +
                "Generated salt for this run: " + salt);
        System.out.println("Please wait, hashing password + salt using Scrypt. \n" +
                "This is taking a long time because I'm trying to thwart a timing analysis attack.");
        String generatedSecuredPasswordHash = com.lambdaworks.crypto.SCryptUtil.scrypt(saltedPassword, 1048576, 8, 1);
        System.out.println("Password hash generated: " + "\n" +generatedSecuredPasswordHash);
        System.out.println("Please reenter password: ");
        password = sc.nextLine();
        boolean matched = com.lambdaworks.crypto.SCryptUtil.check(password + salt, generatedSecuredPasswordHash);
        System.out.println("Rehashing, please wait...");
        System.out.println("Password reentry + salt matches original entry :" + matched);
        matched = com.lambdaworks.crypto.SCryptUtil.check("passwordno", generatedSecuredPasswordHash);
        System.out.println(matched);
    }

    /**
     * Prompts for and reads the name of an input file from the user. Requires file extension.
     * Valid: c:\Test.txt | \\server\shared\Test.txt | \\server\shared\Test.t
     * Invalid: c:\Test | \\server\shared | \\server\shared\Test.?
     *
     * https://regexlib.com/REDetails.aspx?regexp_id=425
     * */
    public void getInputFilePath() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a path to an input file: ");
        String input = sc.nextLine();
        String regex = "^([a-zA-Z]\\:|\\\\\\\\[^\\/\\\\:*?\"<>|]+\\\\[^\\/\\\\:*?\"<>|]+)(\\\\[^\\/\\\\:*?\"<>|]+)+(\\.[^\\/\\\\:*?\"<>|]+)$";

        //first check to see if filepath is of valid format.
        while (checkPattern(input, regex)) {
            sc.nextLine();
            System.out.println("No...Please...Stop...Please enter a valid path to an input file: ");
        };

        //now check to see if that file actually exists.
        File inputFile = new File(input);
        while (!inputFile.exists() || inputFile.isDirectory()) {
            sc.nextLine();
            System.out.println("If you're trying to break me you'll have to do better than that... :)");
        }
        myInputFile = inputFile;
    }

    /**
     * Prompts for and reads the name of an output file from the user. Requires file extension.
     * Valid: c:\Test.txt | \\server\shared\Test.txt | \\server\shared\Test.t
     * Invalid: c:\Test | \\server\shared | \\server\shared\Test.?
     *
     * https://regexlib.com/REDetails.aspx?regexp_id=425
     * */
    private void getOutPutFile() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a filepath to the location you wish to save the output file to: ");
        String input = sc.nextLine();
        String regex = "^([a-zA-Z]\\:|\\\\\\\\[^\\/\\\\:*?\"<>|]+\\\\[^\\/\\\\:*?\"<>|]+)(\\\\[^\\/\\\\:*?\"<>|]+)+(\\.[^\\/\\\\:*?\"<>|]+)$";
        //first check to see if filepath is of valid format.
        while (checkPattern(input, regex)) {
            sc.nextLine();
            System.out.println("java.io.FileNotFoundException haha just kidding please enter a valid filepath to the output file: ");
        };
        //now check to see if that file actually exists.
        File outputFile = new File(input);
        while (!outputFile.exists() || outputFile.isDirectory()) {
            sc.nextLine();
            System.out.println("I spent all of that time making this nice output file for you and you just pull it out from under me like that...rude");
            getOutPutFile();
        }
        myOutputFile = outputFile;
    }


    /**
     * RegEx pattern checker.
     * @param theInputString is the string to match.
     * @param theRegex is the regex to match against.
     */
    private boolean checkPattern(final String theInputString, final String theRegex){
        Pattern pattern = Pattern.compile(theRegex);
        Matcher matcher = pattern.matcher(theInputString);
        boolean matches = matcher.matches();
        System.out.println("Matches input requirements: " + matches);
        return matches;
    }

}
