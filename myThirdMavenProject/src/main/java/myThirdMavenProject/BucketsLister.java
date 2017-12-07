package myThirdMavenProject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

/**
 * This is a helper class that contains methods to retrieve information from the
 * Cloud Storage and present it in the GUI
 * 
 * @author espinajohn
 * 
 */
public class BucketsLister {

	/**
	 * This method retrieves all the Buckets and present them as a dropdown menu
	 * 
	 * @param cloudManagerSDK
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ComboBox listBucketsInMenu(CloudManagerSDK cloudManagerSDK)
			throws FileNotFoundException, IOException {

		ArrayList<String> buckets = new ArrayList<String>();

		buckets = cloudManagerSDK.retrieveBuckets();

		ObservableList<String> obsBucket = FXCollections
				.observableArrayList(buckets);

		ComboBox menu = new ComboBox(obsBucket);

		menu.setVisibleRowCount(obsBucket.size());

		return menu;

	}

	/**
	 * This method retrieves all Buckets and presnt them as checkboxes
	 * 
	 * @param cloudManagerSDK
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static CheckBox[] listBucketsWithCheckBox(
			CloudManagerSDK cloudManagerSDK) throws FileNotFoundException,
			IOException {

		ArrayList<Bucket> buckets = new ArrayList<Bucket>();
		buckets = cloudManagerSDK.retrieveBucketsAsBucket();
		final CheckBox[] cbs = new CheckBox[buckets.size()];

		// loop through the list and create the checkbox inside the loop
		for (int i = 0; i < buckets.size(); i++) {

			System.out.println(buckets.get(i));
			final CheckBox checkBox = cbs[i] = new CheckBox(buckets.get(i)
					.getName());
			checkBox.selectedProperty().addListener(
					new ChangeListener<Boolean>() {
						public void changed(
								ObservableValue<? extends Boolean> ov,
								Boolean old_val, Boolean new_val) {

						}
					});

		}
		return cbs;

	}

	/**
	 * This method will create checkboxes of blobs
	 * @param cloudManagerSDK
	 * @param bucketName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static CheckBox[] listBlobsWithCheckBox(
			CloudManagerSDK cloudManagerSDK, String bucketName)
			throws FileNotFoundException, IOException {

		ArrayList<Blob> blobs = new ArrayList<Blob>();

		blobs = cloudManagerSDK.getBlobs(bucketName);

		final CheckBox[] cbs = new CheckBox[blobs.size()];

		// loop through the list and create the checkbox inside the loop
		for (int i = 0; i < blobs.size(); i++) {

			System.out.println(blobs.get(i));
			final CheckBox checkBox = cbs[i] = new CheckBox(blobs.get(i)
					.getName());
			checkBox.selectedProperty().addListener(
					new ChangeListener<Boolean>() {
						public void changed(
								ObservableValue<? extends Boolean> ov,
								Boolean old_val, Boolean new_val) {

						}
					});

		}
		return cbs;

	}

}
