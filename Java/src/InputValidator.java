import java.io.*;
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

    /** String to represent first name. */
    private String myFirstName;
    /** String to represent last name. */
    private String myLastName;
    /** An integer. */
    private int myFirstInt;
    /** Another integer. */
    private int mySecondInt;
    /** Result of adding the two integers. */
    private int mySum;
    /** Result of multiplying the two integers. */
    private int myProduct;
    /** Input file obtained from input file method. */
    private BufferedReader myInputFile;
    /** Output file specified by use.r */
    private BufferedWriter myOutputFile;
    /** The result of hashing a user-supplied password combined with a secure salt. */
    private String myPassword;
    /** A secure random integer used as salt for password hashing. */
    private int mySalt;


    /** Constructor */
    public InputValidator() throws IOException {
        getFirstName();
        getLastName();
        getIntOne();
        getIntTwo();
        getInputFilePath();
        getOutPutFile();
        getPassword();
        addIntegers();
        multiplyIntegers();
        writeOutputFile();
    }

    /** Gets first name string. Allows for international characters
     * with valid range between 2 to 50 characters. */
    private void getFirstName() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your first name: ");
        String regex = "^([a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð,'-]{2,50})$";
        String input = sc.nextLine();

        while (!checkPattern(input, regex)) {
            System.out.println("Im being really generous with what's allowed here... Please enter a valid first name... ");
            input = sc.nextLine();
        }
        myFirstName = input;
    }

    /** Gets first name string. Allows for international characters
     * with valid range between 2 and 50 characters. */
    private void getLastName() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your last name: ");
        String regex = "^([a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð,'-]{2,50})$";
        String input = sc.nextLine();

        while (!checkPattern(input, regex)) {
            System.out.println("You're breaking my heart... Please enter a valid last name... ");
            input = sc.nextLine();
        }
        myLastName = input;
    }

    /** Gets an integer. */
    private void getIntOne() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter an integer: ");

        while (!sc.hasNextInt()) {
            System.out.println("But...I thought we were friends... Please enter a valid integer. ");
        }
        myFirstInt = sc.nextInt();
    }

    /**Gets another integer.  */
    public void getIntTwo() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter another integer: ");
        while (!sc.hasNextInt()) {
            System.out.println("Please enter your favorite color- wait I mean INTEGER... Please enter a valid integer...");
        }
        mySecondInt = sc.nextInt();
    }

    /** Checks to see if adding the two integers overflows or underflows. Requests new integers
     * if overflow or underflow occurs. */
    public void addIntegers() {
        while ((myFirstInt > 0) && (mySecondInt > Integer.MAX_VALUE - myFirstInt) ||
                ((myFirstInt < 0) && (mySecondInt < Integer.MIN_VALUE - myFirstInt))) {
            System.out.println("Are you trying to spill something?");
            getIntOne();
            getIntTwo();
        }
        mySum = myFirstInt + mySecondInt;
    }

    /** Checks to see if integers can be multiplied on two's complement architecture, and also
     * checks to see that multiplication of the two integers will not cause overflow or underflow.
     * Requests new integers if overflow or underflow will occur. */
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

    /** Gets a password from the user according to specific parameters. Uses the
     * Scrypt password hashing algorithm from lambdaworks. Parameters are set with
     * N: General work factor, iteration count.
     * r: blocksize in use for underlying hash; fine-tunes the relative memory-cost.
     * p: parallelization factor; fine-tunes the relative cpu-cost.
     * https://stackoverflow.com/questions/11126315/what-are-optimal-scrypt-work-factors
     * 48as4tAa1!48as4tAa1!
     */
    public void getPassword() {

        String passwordRequirements = """
                Please enter a password that contains at least:
                   * 10 characters
                   * one upper case character,
                   * lower case character,
                   * digit,
                   * punctuation mark,
                and does not have more than 3 consecutive lower case characters:""";


        final SecureRandom random = new SecureRandom();
        mySalt = random.nextInt(Integer.MAX_VALUE);
        String regex = "^(?=.*{10,}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W])(?!.*([a-z])\\1{2}).*$";
        System.out.println(passwordRequirements);
        Scanner sc = new Scanner(System.in);
        String password = sc.nextLine();
        while (!checkPattern(password, regex)) {
            System.out.println(passwordRequirements);
            password = sc.nextLine();
        }
        String saltedPassword = password + mySalt;

        System.out.println("Generated salt for this run: " + mySalt);
        System.out.println("Please wait, hashing password + salt using Scrypt... \n" +
                "This is taking a long time because I'm trying to thwart a timing analysis attack...");
        String generatedSecuredPasswordHash = com.lambdaworks.crypto.SCryptUtil.scrypt(saltedPassword, 1048576, 8, 1);
        System.out.println("Password hash generated: " + "\n" +generatedSecuredPasswordHash);
        System.out.println("Please reenter password: ");

        password = sc.nextLine();
        System.out.println("Rehashing, please wait...");
        boolean matched = com.lambdaworks.crypto.SCryptUtil.check(password + mySalt, generatedSecuredPasswordHash);
        while (!matched) {
            System.out.println("Password reentry + salt matches original entry: " + matched);
            System.out.println("Please reenter password: ");
            password = sc.nextLine();
            System.out.println("Rehashing, please wait...");
            matched = com.lambdaworks.crypto.SCryptUtil.check(password + mySalt, generatedSecuredPasswordHash);
        }

        myPassword = password;
        System.out.println("Password reentry + salt matches original entry: " + matched);
        System.out.println("Checking something different against password hash, please wait...");
        matched = com.lambdaworks.crypto.SCryptUtil.check("passwordno", generatedSecuredPasswordHash);
        System.out.println("Something different matches password hash: " + matched);
        sc.close();
    }

    /**
     * Prompts for and reads the name of an input file from the user. Requires file extension.
     * Valid: c:\Test.txt | \\server\shared\Test.txt | \\server\shared\Test.t
     * Invalid: c:\Test | \\server\shared | \\server\shared\Test.?
     *
     * https://regexlib.com/REDetails.aspx?regexp_id=425
     * */
    public void getInputFilePath() throws FileNotFoundException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a path to an input file: ");
        String input = sc.nextLine();
        String regex = "^.*\\.txt$";

        //first check to see if filepath is of valid format.
        while (!checkPattern(input, regex)) {
            System.out.println("No...Please...Stop...Please enter a valid path to an input file: ");
            input = sc.nextLine();
        }

        //now check to see if that file actually exists.
        File inputFile = new File(input);
        while (!inputFile.exists() || inputFile.isDirectory()) {
            System.out.println("If you're trying to break me you'll have to do better than that... :)");
            input = sc.nextLine();
        }
        myInputFile = new BufferedReader(new FileReader(input));
    }

    /**
     * Prompts for and reads the name of an output file from the user. Requires file extension.
     * Valid: c:\Test.txt | \\server\shared\Test.txt | \\server\shared\Test.t
     * Invalid: c:\Test | \\server\shared | \\server\shared\Test.?
     *
     * https://regexlib.com/REDetails.aspx?regexp_id=425
     * */
    private void getOutPutFile() throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a filepath to the location you wish to save the output file to: ");
        String input = sc.nextLine();
        String regex = "^.*\\.txt$";
        //first check to see if filepath is of valid format.
        while (!checkPattern(input, regex)) {
            System.out.println("java.io.FileNotFoundException haha just kidding please enter a valid filepath to the output file: ");
            input = sc.nextLine();
        }
        //now check to see if that file actually exists.
        File outputFile = new File(input);

        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        while (outputFile.isDirectory()) {
            System.out.println("I spent all of that time making this nice output file for you and you just pull it out from under me like that...rude");
            input = sc.nextLine();
            getOutPutFile();
        }
        FileWriter fw = new FileWriter(input);
        myOutputFile = new BufferedWriter(fw);
    }

    /** Writes the data obtained in this class line by line to the specified output file. */
    public void writeOutputFile() throws IOException {

        myOutputFile.write("First and last name: \n" + myFirstName + " " + myLastName + "\n\n");
        myOutputFile.write("User-specified integer #1: \n" + myFirstInt + "\n\n");
        myOutputFile.write("User-specified integer #2: \n" + mySecondInt + "\n\n");
        myOutputFile.write("Result of adding the two integer values: \n" + mySum + "\n\n");
        myOutputFile.write("Result of multiplying the two integer values: \n" + myProduct + "\n\n");
        myOutputFile.write("Contents of the input file: \n");
        myInputFile.lines().forEach(line -> {
            try {
                myOutputFile.write(line);
                myOutputFile.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        myOutputFile.flush();
        myOutputFile.close();
    }


    /**
     * RegEx pattern checker.
     * @param theInputString is the string to match.
     * @param theRegex is the regex to match against.
     */
    private boolean checkPattern(final String theInputString, final String theRegex){
        Pattern pattern = Pattern.compile(theRegex);
        Matcher matcher = pattern.matcher(theInputString);
        //        System.out.println("Matches input requirements: " + matches);
        return matcher.find();
    }

}
