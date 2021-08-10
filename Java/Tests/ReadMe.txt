Team Members: 	Kittera McCloud
		Dustin Ray
		Brandon Rosario

Java version: JDK 16
Java IDE used: Intellij 2021.2
java external library: lambdaworks
Python version: 3.9.2
Python IDE used: PyCharm 2021.2

	All in all, we figure there are no real large shortcomings within our code. There was some discussion about whether or not first and last names should be allowed if they do not have a vowel within, but that seemed a bit nitpicky. Besides that, the issue does not crash the code or pose any security risks. Although we check for a “.txt” file extension for both the input and output file within the python version of our code, we do not check for the extension in the java version of the solution. This is because for the input file, the file must exist, and any file is allowed. We do check for valid file names and windows restricted characters, however. For the output file in our java solution, we have allowed the user to input any file name that is legal in the windows operating system but does not require a file extension. In our Python solution we have decided to encode the data being written to the text file with UTF-8. This means that we need to check whether the input file being read to the file is also UTF-8. Otherwise, the program will crash if an incorrect started code is presented. In our Java solution, we decided not to encode the data and have all the data pass through into the output file. This causes no issues within the program. Among these thoughts and concerns, we did not have enough time to test several file extensions extensively, and nor would have been able to. 
	
As for the issues that we had accounted for:

		Name:
			The user’s name input is limited to 50 characters and most special characters are not allowed (special characters ‘ and – are allowed). In our Java solution, we also allowed quite a few foreign characters.

		Integers:
			Integers are checked using the scanner object to ensure the correct primitive data type. The integers are then added and multiplied using the BigInteger object to ensure that overflow does not occur after the operation.

		File names:
			File names are required to have the .txt extension within the Python solution. File names must adhere to Window’s file name guidelines for the Java solution. The input file also must be within the source directory or must have a full pathname. Correct data must be within the file for our python solution as well. For both solutions, if the output file does not exist a file will be created within the directory.

		Password:
			For both the solutions we used the regex for the regex assignment concerning passwords. For both solutions, the password is salted and hashed before being compared to the reentered password. For both solutions, side-channel attack prevention was emplaced by lengthening the time it took for the password to be verified. For the hashing algorithm we used Scrypt for both the Java and Python solution. When the password is entered into the program the hashed password is then stored to an external file and retrieved to verify the entered passwords legitimacy.



Testing: 
	For testing we tested the regex of all the methods that were used. This means we did not test the program for full coverage; however, all user inputs are passed through regex validation and that comprises most of our security checks. We have tested for all the edge cases that we could think of and all test passes. 

Error logs:
	Whenever an invalid entry is inputted into the program, the date, time and event is logged within an external file.
