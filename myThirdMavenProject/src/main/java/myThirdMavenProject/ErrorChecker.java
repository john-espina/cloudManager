package myThirdMavenProject;

import javafx.scene.control.Alert;

/**
 * This is class that contains a method that will check the errors thrown in the program
 * @author espinajohn
 *
 */
public class ErrorChecker {
	
	
	/**
	 * This method checks for the error code returned from the implemented try/catch functionality.
	 * Each error code is presented to the user as an easily understandable string/message.
	 * @param errorCode
	 * @param alert
	 */
	static void check(int errorCode, Alert alert){
		if (errorCode == 409) {
			alert.setContentText("You already own this bucket. Please select another name");
			alert.showAndWait();
		}

		if (errorCode == 101) {
			alert.setContentText("File Not Found. Please Check the Path");
			alert.showAndWait();
		}

		if (errorCode == 400) {
			alert.setContentText("Unknown Project ID. Please review Project ID");
			alert.showAndWait();
		}
		
		if (errorCode == 1000) {
			alert.setContentText("Please supply missing information");
			alert.showAndWait();
		}
		
		if (errorCode == 5000) {
			alert.setContentText("Unable to process request. Please review the details provided");
			alert.showAndWait();
		}
		
		if (errorCode == 6000) {
			alert.setContentText("Please check credentials");
			alert.showAndWait();
		}
		
		if (errorCode == 7000) {
			alert.setContentText("Please check the inputted files");
			alert.showAndWait();
		}
	}

}
