package myThirdMavenProject;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Bucket.BucketSourceOption;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.CopyWriter;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.Storage.BucketListOption;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.StorageOptions.Builder;

/**
 * This Class is a Java implementation of Google Cloud Console It uses Maven
 * framework and has Google SDK as a dependency. It uses GoogleCredentials as
 * access control.
 * 
 * @author espinajohn
 * @author espinajohn
 * 
 */
public class CloudManagerSDK {

	// Fields
	private String projectID;
	private String pathToCredentials;
	private String errorCode;

	// Constructor
	public CloudManagerSDK(String projectID, String path) {
		this.projectID = projectID;
		this.pathToCredentials = path;
	}

	/**
	 * This method will verify user credentials and instantiate a new Storage
	 * object
	 * 
	 * @return
	 */
	protected Storage storageBuilder() {

		Storage storage = null;

		try {

			// Create a Builder
			Builder builder = StorageOptions.newBuilder();

			// Set the credentials
			builder.setCredentials(ServiceAccountCredentials
					.fromStream(new FileInputStream(getPathToCredentials())));

			// identify the project ID to work on
			builder.setProjectId(getProjectID());

			// get the storage of the project
			storage = builder.build().getService();

		} catch (FileNotFoundException e) {

			this.setErrorCode("101");

		} catch (IOException e) {

			e.printStackTrace();
		}

		return storage;

	}

	/**
	 * Method to create a new bucket
	 * 
	 * @param bucketName
	 */
	public Bucket createBucket(String bucketName, String location) {

		// get the project's storage
		Storage storage = storageBuilder();

		// create the new bucket by calling
		Bucket bucket = storage.create(BucketInfo.newBuilder(bucketName)
				.setLocation(location).build());

		this.setErrorCode("0");

		return bucket;

	}

	/**
	 * This method will create a bucket without location as a parameter
	 * 
	 * @param bucketName
	 * @return
	 */
	public Bucket createBucket(String bucketName) {

		Storage storage = storageBuilder();
		Bucket bucket = null;

		bucket = storage.create(BucketInfo.of(bucketName));

		this.setErrorCode("0");

		return bucket;

	}

	/**
	 * Method that retrieve all the buckets (as String) in the specified storage
	 * and place them in an arraylist. It is retrieved as a String to be used in
	 * presenting as drop down menu or checkboxes
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ArrayList<String> retrieveBuckets() throws FileNotFoundException,
			IOException {

		// Initialise an ArrayList of buckets
		ArrayList<String> buckets = new ArrayList<String>();

		// Get the project's storage to work on
		Storage storage = storageBuilder();

		// Iterate through the project and add every bucket names to the list
		for (Bucket currentBucket : storage.list().iterateAll()) {
			buckets.add(currentBucket.getName());
		}

		return buckets;
	}

	/**
	 * This bucket retrieve all buckets from a specified project
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Bucket> retrieveBucketsAsBucket()
			throws FileNotFoundException, IOException {

		// Initialise an ArrayList of Buckets
		ArrayList<Bucket> buckets = new ArrayList<Bucket>();

		// Get the project's storage to work on
		Storage storage = storageBuilder();

		// Iterate through the project's storage and add each bucket to the list
		// of buckets
		for (Bucket currentBucket : storage.list().iterateAll()) {
			buckets.add(currentBucket);
		}

		return buckets;
	}

	/**
	 * This method will upload a blob to a specified bucket
	 * 
	 * @param cloudManagerSDK
	 * @param bucketName
	 * @param blobname
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void uploadBlob(CloudManagerSDK cloudManagerSDK, String bucketName,
			String blobname, String pathToFile) throws FileNotFoundException,
			IOException {

		// Get the project's storage to work on
		Storage storage = storageBuilder();

		// Identify the file's path to upload
		Path path = Paths.get(pathToFile);

		// Get the bytes of the file
		byte[] data = Files.readAllBytes(path);

		// Wrap the byte as InputStream
		InputStream content = new ByteArrayInputStream(data);

		// Create a Blob Id
		BlobId blobId = BlobId.of(bucketName, blobname);

		// Add info to the blob
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
				.setContentType("text/plain").build();

		// Create the blob using above's indo/data
		Blob blob = storage.create(blobInfo, content);

	}

	/**
	 * This method merge selected buckets including the Blobs inside it.
	 * Selected buckets will be deleted.
	 * 
	 * @param buckets
	 * @param bucketName
	 */
	public void mergeBuckets(ArrayList<String> buckets, String bucketName) {

		// Get the project's storage to work on
		Storage storage = storageBuilder();

		// Create a new bucket where the two buckets for merging will be moved
		// Create the new bucket using user inputted name
		Bucket newBucket = createBucket(bucketName);

		// Initialise a List of buckets for merging
		ArrayList<Bucket> bucketsForMerge = new ArrayList<Bucket>();

		// Retrieve the buckets for merging from the storage
		// Use names ( String) from the ArrayList of buckets to match the bucket
		// that will be retrieved from the storage
		// Add these buckets into the List of buckets to be merged
		for (int i = 0; i < buckets.size(); i++) {

			bucketsForMerge.add(storage.get(buckets.get(i)));
		}

		// Now loop through all the buckets in the list of buckets to be merged
		for (int i = 0; i < bucketsForMerge.size(); i++) {

			// Get the blob inside each bucket and copy it to the new bucket
			// Delete the blob after copying it
			for (Blob currentBlob : bucketsForMerge.get(i).list().iterateAll()) {

				CopyWriter copyWriter = currentBlob.copyTo(newBucket.getName());
				storage.delete(currentBlob.getBlobId());
			}
		}

		// At this point, each bucket inside the List of buckets to be merged is
		// empty and can now be deleted
		for (int i = 0; i < bucketsForMerge.size(); i++) {
			bucketsForMerge.get(i).delete();
		}

	}

	/**
	 * This method retrieves all the blobs in a bucket
	 * 
	 * @param bucketName
	 * @return
	 */
	public ArrayList<Blob> getBlobs(String bucketName) {

		// retrieve project's strorage to work on
		Storage storage = storageBuilder();

		// Initialise a bucket
		Bucket bucket = null;

		// Search for the requested bucket by matching the name inputted by the
		// user
		// If it matches, the matched bucket will be passed on to
		// the already initialised bucket above
		for (Bucket currentBucket : storage.list().iterateAll()) {
			if (bucketName.equals(currentBucket.getName())) {
				bucket = currentBucket;
			}
		}

		// Initialise a new ArrayList of Blob
		ArrayList<Blob> blobs = new ArrayList<Blob>();

		// Using the Bucket, iterate through all the blobs inside the bucket and
		// add it to the list
		for (Blob currentBlob : bucket.list().iterateAll()) {
			blobs.add(currentBlob);
		}

		return blobs;
	}

	/**
	 * This method copies selected blobs inside a specified bucket and moved it
	 * to a new bucket The unselected blobs will be moved to a different new
	 * bucket; hence the specified bucket is splitted The specified bucket will
	 * then be deleted
	 * 
	 * @param blobsToMove1
	 * @param blobsToMove2
	 * @param bucketName1
	 * @param bucketName2
	 * @param bucketString
	 */
	public void splitAndMove(ArrayList<String> blobsToMove1,
			ArrayList<String> blobsToMove2, String bucketName1,
			String bucketName2, String bucketString) {

		Storage storage = storageBuilder();

		// Create a new bucket using user inputted new name
		Bucket newBucket1 = createBucket(bucketName1);

		// Create a second new bucket using user inputted name
		Bucket newBucket2 = createBucket(bucketName2);

		// Initialise a bucket that will be used later as the bucket specified
		// to be splitted
		Bucket bucket = null;

		// Search for the specified bucket by matching the name inputted by the
		// user
		for (Bucket currentBucket : storage.list().iterateAll()) {
			if (bucketString.equals(currentBucket.getName())) {
				bucket = currentBucket;
			}
		}

		// Iterate through all the selected blobs and match it with blobs inside
		// the specified bucket
		for (Blob currentBlob : bucket.list().iterateAll()) {

			for (int i = 0; i < blobsToMove1.size(); i++) {

				// If the blob exists, copy it to the new bucket (first one) and
				// then delete it
				if (blobsToMove1.get(i).equals(currentBlob.getName())) {
					CopyWriter copyWriter = currentBlob.copyTo(newBucket1
							.getName());
					storage.delete(currentBlob.getBlobId());
				}
			}

		}

		// Iterate through all the unselected blobs and match it with the
		// specified bucket
		for (Blob currentBlob : bucket.list().iterateAll()) {

			for (int i = 0; i < blobsToMove2.size(); i++) {

				// If the blob exist in the specified bucket, copy it to the new
				// bucket (second one) and delete it
				if (blobsToMove2.get(i).equals(currentBlob.getName())) {
					CopyWriter copyWriter = currentBlob.copyTo(newBucket2
							.getName());
					storage.delete(currentBlob.getBlobId());
				}
			}

		}

		// At this point, the bucket is empty, so it can now be deleted
		bucket.delete();

	}

	// Getters and Setters

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public String getPathToCredentials() {
		return pathToCredentials;
	}

	public void setPathToCredentials(String pathToCredentials) {
		this.pathToCredentials = pathToCredentials;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
