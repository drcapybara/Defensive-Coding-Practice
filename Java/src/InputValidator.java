import java.io.*;
import java.math.BigInteger;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
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
 * Methods are intentionally left redundant to demonstrate the flow of information as it moves through
 * the program.
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
    private BigInteger mySum;
    /** Result of multiplying the two integers. */
    private BigInteger myProduct;
    /** Input file obtained from input file method. */
    private BufferedReader myInputFile;
    /** Output file specified by use.r */
    private BufferedWriter myOutputFile;


    /** Constructor */
    public InputValidator() throws IOException {
        getFirstName();
        getLastName();
        getIntOne();
        getIntTwo();
        getInputFilePath();
        getOutPutFile();
        addIntegers();
        multiplyIntegers();
        getPassword();
        writeOutputFile();
    }

    /** Gets first name string.
     * with valid range between 2 to 50 characters. */
    private void getFirstName() {

        String nameRequirements = """
                Please enter your first name:
                Valid characters can be a-z, A-Z, -'
                Minimum 2 characters required, maximum of 50 allowed:""";

        Scanner sc = new Scanner(System.in);
        System.out.println(nameRequirements);
        String regex = "^([a-zA-Z-']{2,50})$";
        String input = sc.nextLine();

        while (checkPattern(input, regex)) {
            System.out.println("Im being really generous with what's allowed here... Please enter a valid first name... ");
            input = sc.nextLine();
        }
        myFirstName = input;


    }

    /** Gets last name string.
     * with valid range between 2 to 50 characters. */
    private void getLastName() {

        String nameRequirements = """
                Please enter your last name:
                Valid characters can be a-z, A-Z, -'
                Minimum 2 characters required, maximum of 50 allowed:""";

        Scanner sc = new Scanner(System.in);
        System.out.println(nameRequirements);
        String regex = "^([a-zA-Z-']{2,50})$";
        String input = sc.nextLine();

        while (checkPattern(input, regex)) {
            System.out.println(nameRequirements);
            input = sc.nextLine();
        }
        myLastName = input;
    }

    /** Gets an integer. */
    private void getIntOne() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter an integer: ");

        while (!sc.hasNextInt()) {
            System.out.println("Please enter a valid integer: ");
            sc.nextLine();
        }
        myFirstInt = sc.nextInt();
    }

    /**Gets another integer.  */
    public void getIntTwo() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter another integer: ");
        while (!sc.hasNextInt()) {
            System.out.println("But...I thought we were friends... Please enter a valid integer. ");
            sc.nextLine();
        }
        mySecondInt = sc.nextInt();
    }

    /** Checks to see if adding the two integers overflows or underflows. Requests new integers
     * if overflow or underflow occurs. */
    public void addIntegers() {

        mySum = new BigInteger(String.valueOf(myFirstInt));
        mySum = mySum.add(new BigInteger(String.valueOf(mySecondInt)));

    }

    /** Checks to see if integers can be multiplied on two's complement architecture, and also
     * checks to see that multiplication of the two integers will not cause overflow or underflow.
     * Requests new integers if overflow or underflow will occur. Takes absolute value of
     * negative input for general case validation purposes. */
    public void multiplyIntegers() {

        myProduct = new BigInteger(String.valueOf(myFirstInt));
        myProduct = myProduct.multiply(BigInteger.valueOf(mySecondInt));
    }

    /** Gets a password from the user according to specific parameters. Uses the
     * Scrypt password hashing algorithm from lambdaworks. Parameters are set with
     * N: General work factor, iteration count.
     * r: blocksize in use for underlying hash; fine-tunes the relative memory-cost.
     * p: parallelization factor; fine-tunes the relative cpu-cost.
     * https://stackoverflow.com/questions/11126315/what-are-optimal-scrypt-work-factors
     * valid password example: 48as4tAa1!48as4tAa1!
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
        int salt = random.nextInt(Integer.MAX_VALUE);
        String regex = "^(?=.*{10,}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W])(?!.*([a-z])\\1{2}).*$";
        System.out.println(passwordRequirements);
        Scanner sc = new Scanner(System.in);
        String password = sc.nextLine();
        while (checkPattern(password, regex)) {
            System.out.println(passwordRequirements);
            password = sc.nextLine();
        }
        String saltedPassword = password + salt;

        System.out.println("Generated salt for this run: " + salt);
        System.out.println("Please wait, hashing password + salt using Scrypt... \n" +
                "This is taking a long time because I'm trying to thwart a timing analysis attack...");
        String generatedSecuredPasswordHash = com.lambdaworks.crypto.SCryptUtil.scrypt(saltedPassword, 1048576, 8, 1);
        System.out.println("Password hash generated: " + "\n" +generatedSecuredPasswordHash);
        System.out.println("Please reenter password: ");

        password = sc.nextLine();
        System.out.println("Rehashing, please wait...");
        boolean matched = com.lambdaworks.crypto.SCryptUtil.check(password + salt, generatedSecuredPasswordHash);
        while (!matched) {
            System.out.println("Password reentry + salt matches original entry: " + false);
            System.out.println("Please reenter password: ");
            password = sc.nextLine();
            System.out.println("Rehashing, please wait...");
            matched = com.lambdaworks.crypto.SCryptUtil.check(password + salt, generatedSecuredPasswordHash);
        }

        System.out.println("Password reentry + salt matches original entry: " + true);
        System.out.println("Checking something different against password hash, please wait...");
        matched = com.lambdaworks.crypto.SCryptUtil.check("passwordno", generatedSecuredPasswordHash);
        System.out.println("Something different matches password hash: " + matched);
        sc.close();
    }

    /** Prompts for and reads the name of an input file from the user. Requires file txt extension. */
    public void getInputFilePath() throws FileNotFoundException {

        String regex = "^(?:(?:[a-z]:|\\\\\\\\[a-z0-9_.$●-]+\\\\[a-z0-9_.$●-]+)\\\\|\\\\?[^\\\\:*?\"<>|↵\\r\\n]+\\\\?)(?:[^\\\\:*?\"<>|\\r\\n]+\\\\)*[^\\\\:*?\"<>|\\r\\n]*$";

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a path to an input file: (File must exist and can be of any format) ");
        String input = sc.nextLine();

        while (checkPattern(input, regex)) {
            System.out.println("Invalid characters in file path, please try again: ");
            input = sc.nextLine();
        }

        try {
            while (!Paths.get(input).toFile().exists()) {
                System.out.println("file does not exist or invalid path was supplied, lets bring it around for another try. Please enter a path to a file: ");
                input = sc.nextLine();
            }
        } catch (InvalidPathException e) {
            System.out.println("Invalid file path entered, please use valid characters especially if you are on windows: ");
            getInputFilePath();
        }

        //now check to see if that file actually exists.
        while (!new File(input).exists() || new File(input).isDirectory()) {
            System.out.println("File does not exist or you supplied a bad path: please enter a valid file path: ");
            input = sc.nextLine();
        }
        myInputFile = new BufferedReader(new FileReader(input));
    }

    /** Prompts for and reads the name of an input file from the user. Requires file txt extension. */
    private void getOutPutFile() throws IOException {

        String regex = "^(?:(?:[a-z]:|\\\\\\\\[a-z0-9_.$●-]+\\\\[a-z0-9_.$●-]+)\\\\|\\\\?[^\\\\:*?\"<>|↵\\r\\n]+\\\\?)(?:[^\\\\:*?\"<>|\\r\\n]+\\\\)*[^\\\\:*?\"<>|\\r\\n]*$";

        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter a path to an output file: File cannot already exist: ");
        String input = sc.nextLine();

        while (checkPattern(input, regex)) {
            System.out.println("Invalid characters in file path, please try again: ");
            input = sc.nextLine();
        }

        while (input.length() == 0) {
            System.out.println("Please enter a valid output filepath, length of path must be greater than zero: ");
            input = sc.nextLine();
        }

        try {
            if (!(new File(input).exists())) {
                boolean result = new File(input).createNewFile();
                if (result) {
                    FileWriter fw = new FileWriter(input);
                    myOutputFile = new BufferedWriter(fw);
                }
            } else {
                System.out.println("Output file already exists, please specify a different output file: ");
                getOutPutFile();
            }
        } catch (InvalidPathException e) {
            System.out.println("Invalid path detected, please try again: ");
            getOutPutFile();
        } catch (IOException e) {
            System.out.println("possible permission violation, please try again and stay the heck away from my sensitive files: ");
            getOutPutFile();
        }
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
                System.out.println("There was a problem writing to the output file. ");
                try {
                    getOutPutFile();
                } catch (IOException ex) {
                    System.out.println("There was a problem writing to the output file. ");
                }
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
        return !matcher.find();
    }

}
