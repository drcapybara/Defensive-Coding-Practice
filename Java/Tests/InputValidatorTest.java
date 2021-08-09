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

        myTestList.add("J");
        myTestList.add("J j");
        myTestList.add("3");
        myTestList.add("!!");
        myTestList.add("-,");
        for (String s : myTestList) {assertFalse(validator(s));}
    }

    /** Tests pairs of integers to make sure that addition will not cause overflow
     * or underflow. */
    @Test
    public void testAddInteger() {
        //add this                          to this
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

    /** Tests pairs of integers to ensure that multiplication will not cause overflow
     * or underflow. */
    @Test
    public void testMultiplyInteger() {
        //multiply this                     by this
        myFirstIntList.add(2147483647);     mySecondIntList.add(0);
        myFirstIntList.add(2147483646);     mySecondIntList.add(1);
        myFirstIntList.add(2147483647);     mySecondIntList.add(1);
        myFirstIntList.add(0);              mySecondIntList.add(2147483647);
        myFirstIntList.add(2);              mySecondIntList.add(2);
        myFirstIntList.add(-2147483647);    mySecondIntList.add(0);
        myFirstIntList.add(-1);             mySecondIntList.add(-2147483646);
        myFirstIntList.add(-2147483647);    mySecondIntList.add(-1);
        myFirstIntList.add(0);              mySecondIntList.add(0);

        for (int i = 0; i < myFirstIntList.size(); i++) {
            if((myFirstIntList.get(i) != 0 && mySecondIntList.get(i) != 0) &&
                    (((myFirstIntList.get(i) == -1) && (mySecondIntList.get(i) == Integer.MIN_VALUE) ||
                            ((mySecondIntList.get(i) == -1) && (myFirstIntList.get(i) == Integer.MIN_VALUE))) ||
                            (Math.abs(myFirstIntList.get(i)) > Math.abs(Integer.MAX_VALUE / mySecondIntList.get(i))) ||
                            ((Math.abs(myFirstIntList.get(i)) < Integer.MIN_VALUE / Math.abs(mySecondIntList.get(i)))))) {
                System.out.println((myFirstIntList.get(i) * mySecondIntList.get(i)));
                fail("Overflow or underflow detected");}
        }
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