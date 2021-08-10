import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    /** Contains list of strings to test. */
    private final ArrayList<String> myTestList;
    /** regex to match strings against */
    private String myRegEx;
    /** An integer list. */
    private final ArrayList<Integer> myFirstIntList;
    /** An integer list. */
    private final ArrayList<Integer> mySecondIntList;

    /** Constructor */
    public InputValidatorTest() {
        myTestList = new ArrayList<>();
        myFirstIntList = new ArrayList<>();
        mySecondIntList = new ArrayList<>();
    }

    /** Init testing structures. */
    @BeforeEach
    void setUp() {
        myTestList.clear();
        myRegEx = "";
    }

    /** Clean up testing structures. */
    @AfterEach
    void tearDown() {
        myTestList.clear();
        myRegEx = "";
    }

    /** Tests for valid first and last name input against specifications. */
    @Test
    void testGetFirstAndLastName() {

        myRegEx = "^([a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð'-]{2,50})$";
        myTestList.add("Jack");
        myTestList.add("jack");
        myTestList.add("JJ");
        myTestList.add("Jack-Jack");
        myTestList.add("Jack'OLantern");
        myTestList.add("Jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
        for (String s : myTestList) {assertTrue(validator(s));}
        myTestList.clear();

        myTestList.add("");
        myTestList.add("J");
        myTestList.add("J j");
        myTestList.add("3");
        myTestList.add("!!");
        myTestList.add("-,)(*&^%$#@$%^&*(&^%$");
        myTestList.add("-AAHDJKSHGDJA(&*S^%E$%D&*(SIAPJDKSNAM,");
        myTestList.add("$s0$140801$bOHF7X7B/S0UVY8aM35Eug==$9EUHiYVZop/7dPkGUr7zNo9JUGLYodb0m8UNB7hKzEY=");

        for (String s : myTestList) {assertFalse(validator(s));}
    }

    /** It seems this approach was not the intention of the assignment. However, if one wanted to implement
     * strict integer addition or multiplication, this approach ensures that valid input is received and that
     * the result should then always be an integer. */
    @Test
    public void testAddInteger() {
        //want to add this                  to this
        myFirstIntList.add(2147483647);     mySecondIntList.add(0);
        myFirstIntList.add(2147483646);     mySecondIntList.add(1);
        myFirstIntList.add(0);              mySecondIntList.add(2147483647);
        myFirstIntList.add(2);              mySecondIntList.add(2);
        myFirstIntList.add(-2147483647);    mySecondIntList.add(0);
        myFirstIntList.add(-2147483646);    mySecondIntList.add(-1);

        for (int i = 0; i < myFirstIntList.size(); i++) {
            if ((myFirstIntList.get(i) > 0) && (mySecondIntList.get(i) > Integer.MAX_VALUE - myFirstIntList.get(i)) ||
                    ((myFirstIntList.get(i) < 0) && (mySecondIntList.get(i) < Integer.MIN_VALUE - myFirstIntList.get(i)))) {
                fail("Overflow or underflow detected");}
        }
    }

    /** It seems this approach was not the intention of the assignment. However, if one wanted to implement
     * strict integer addition or multiplication, this approach ensures that valid input is received and that
     * the result should then always be an integer. */
    @Test
    public void testMultiplyInteger() {
        //want to multiply this             by this
        myFirstIntList.add(2147483647);     mySecondIntList.add(0);
        myFirstIntList.add(2147483646);     mySecondIntList.add(1);
        myFirstIntList.add(2147483647);     mySecondIntList.add(1);
        myFirstIntList.add(0);              mySecondIntList.add(2147483647);
        myFirstIntList.add(2);              mySecondIntList.add(2);
        myFirstIntList.add(-2147483647);    mySecondIntList.add(0);
        myFirstIntList.add(-1);             mySecondIntList.add(-2147483646);
        myFirstIntList.add(-2147483646);    mySecondIntList.add(-1);
        myFirstIntList.add(-2147483647);    mySecondIntList.add(-1);
        myFirstIntList.add(0);              mySecondIntList.add(0);

        for (int i = 0; i < myFirstIntList.size(); i++) {
            if((myFirstIntList.get(i) != 0 && mySecondIntList.get(i) != 0) &&
                    (((myFirstIntList.get(i) == -1) && (mySecondIntList.get(i) == Integer.MIN_VALUE) ||
                            ((mySecondIntList.get(i) == -1) && (myFirstIntList.get(i) == Integer.MIN_VALUE))) ||
                            (Math.abs(myFirstIntList.get(i)) > Math.abs(Integer.MAX_VALUE / mySecondIntList.get(i))) ||
                            (myFirstIntList.get(i) < Integer.MIN_VALUE / Math.abs(mySecondIntList.get(i))))) {
                System.out.println((myFirstIntList.get(i) * mySecondIntList.get(i)));
                fail("Overflow or underflow detected");}
        }
    }


    /**
     * Uses RegEx to validate theInputString as a password that contains at least
     *                10 characters
     *                and includes at least:
     *                one upper case character,
     *                lower case character,
     *                digit,
     *                punctuation mark,
     *                and does not have more than 3 consecutive lower case characters
     *
     */
    @Test
    public void testPassword() {

        myRegEx = "^(?=.{10,}$)(?=\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*([a-z])\\1{2}).*$";

        myTestList.add("48as4tAaa1!");
        myTestList.add("48as4tA1!!!");
        myTestList.add("48as4tAa1!48as4tAa1!");
        myTestList.add("48as4tAa1[");
        myTestList.add("48as4tAa1!");

        for (String s : myTestList) {assertTrue(validator(s));}

        myTestList.clear();
        myTestList.add("8as4tAa1!");
        myTestList.add("48as4tAaaa1!");
        myTestList.add("48as4tAaaa1!");
        myTestList.add("48as4tAa1");
        myTestList.add("AAAAA");
        myTestList.add("AAAAa");
        myTestList.add("1");
        myTestList.add("12345");

        for (String s : myTestList) {assertFalse(validator(s));}

    }

    @Test
    public void testFilePath() {


    }





    /**
     * I am validator. Feed me input.
     * @param theInString string to match against myRegex
     * @return true if match found, false otherwise.
     */
    private boolean validator(String theInString) {

        Pattern pattern = Pattern.compile(myRegEx);
        Matcher matcher = pattern.matcher(theInString);
        boolean found = false;
        while (matcher.find()) {
            found = true;
        }
        return found;
    }
}