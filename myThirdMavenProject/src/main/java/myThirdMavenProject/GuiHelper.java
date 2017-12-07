package myThirdMavenProject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class contains methods that helps in setting up new Window pane in the GUI
 * @author espinajohn
 *
 */
public class GuiHelper {

	
	/**
	 * This method adds a new window scene
	 * @param primaryStage
	 * @return
	 */
	public static Group addScene(Stage primaryStage) {
		
		Group root = new Group();
		Scene newView = new Scene(root, 500, 500);
		primaryStage.setScene(newView);
		
		return root;
	}
	
	
	/**
	 * This method will set up the Text and TextField that will ask user of his/her credentials
	 * @param creationBox
	 * @param projectText
	 * @param projectId
	 * @param credentialText
	 * @param credentialPath
	 */
	public static void askCredentials(GridPane creationBox, Text projectText, TextField projectId, Text credentialText, TextField credentialPath){
		
		creationBox.setConstraints(projectText, 0, 0);
		creationBox.getChildren().add(projectText);
		creationBox.setConstraints(projectId, 1, 0);
		creationBox.getChildren().add(projectId);
		creationBox.setConstraints(credentialText, 0, 2);
		creationBox.getChildren().add(credentialText);
		creationBox.setConstraints(credentialPath, 1, 2);
		creationBox.getChildren().add(credentialPath);
		
	}
	
	
	/**
	 * This method lays out the GridPane for every new Window
	 * @return
	 */
	public static GridPane addGridpane(){
		
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(15, 15, 15, 15));
		gridPane.setAlignment(Pos.TOP_CENTER);
		gridPane.setVgap(15);
		gridPane.setHgap(15);
		
		return gridPane;
	}
	
	/**
	 * This method will set a dropbox option of ways to create a bucket
	 * @return
	 */
	public static ComboBox methodOptions()  {

		ArrayList<String> options = new ArrayList<String>();
		options.add("Use SDK");
		options.add("Use Google API");
		ObservableList<String> methodLists = FXCollections.observableArrayList(options);
		ComboBox menu = new ComboBox(methodLists);
		menu.setVisibleRowCount(options.size());
		menu.setPrefWidth(200);

		return menu;

	}
	
	/**
	 * This method will set a dropdown option of locations to store the buckets
	 * @return
	 */
	public static ComboBox locationOptions()  {

		 List<String> locations = new ArrayList<String>(Arrays.asList("australia-southeast1",
	            "us-central1",
	            "us-east1",
	            "us-east4",
	            "us-west1",
	            "southamerica-east1",
	            "europe-west1",
	            "europe-west2",
	            "europe-west3",
	            "asia-east1",
	            "asia-northeast1",
	            "asia-south1",
	            "asia-southeast1"));
		 
		ObservableList<String> locationLists = FXCollections.observableArrayList(locations);
		ComboBox menu = new ComboBox(locationLists);
		menu.setVisibleRowCount(locations.size());
		menu.setPrefWidth(200);

		return menu;

	}
	

	

}
