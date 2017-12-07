package myThirdMavenProject;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.http.client.HttpResponseException;

import com.google.api.Page;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.IOUtils;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.CopyWriter;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.Storage.BucketListOption;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.StorageOptions.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.logging.type.HttpRequest;

/**
 * This Class is a Java implementation of Google Cloud Console It uses Google's
 * Cloud Storage API to perform functions like creating a new bucket or
 * retrieving specified bucket. It uses GoogleCredentials as access control.
 * 
 * @author espinajohn
 * 
 */
public class CloudManagerAPI {

	// Fields
	private String projectID;
	private String pathToCredentials;
	private int errorCode;

	// Constructor
	public CloudManagerAPI(String projectID, String path) {
		this.projectID = projectID;
		this.pathToCredentials = path;
	}

	private static final String STORAGE_SCOPE = "https://www.googleapis.com/auth/devstorage.full_control";

	/**
	 * This method will create a new Bucket using Google's API. The method
	 * creates a hashmap of the properties of the bucket and send this as a
	 * request body
	 * 
	 * @param pathToCredentials
	 * @param projectName
	 * @param bucketNameToCreate
	 * @param location
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public void createBucket(String pathToCredentials, String projectName,
			String bucketNameToCreate, String location) throws IOException,
			GeneralSecurityException {

		GoogleCredential credentials = null;
		try {
			credentials = GoogleCredential.fromStream(
					new FileInputStream(pathToCredentials)).createScoped(
					Collections.singleton(STORAGE_SCOPE));
		} catch (FileNotFoundException f) {
			this.setErrorCode(101);
		}

		String bucketName = bucketNameToCreate;

		try {

			String uri = "https://www.googleapis.com/storage/v1/b?project="
					+ projectName;

			System.out.println(uri);

			HttpTransport httpTransport = GoogleNetHttpTransport
					.newTrustedTransport();

			HttpRequestFactory requestFactory = httpTransport
					.createRequestFactory(credentials);

			GenericUrl url = new GenericUrl(uri);

			System.out.println(url);

			HttpContent content = new JsonHttpContent(new JacksonFactory(),
					createJSONObject(bucketName, location));

			com.google.api.client.http.HttpRequest request = requestFactory
					.buildPostRequest(url, content);

			HttpResponse response = request.execute();

			int rs = response.getStatusCode();

			System.out.println(rs);
			System.out.flush();

			System.out.println("The bucket has been created.\n");
		} catch (UnsupportedEncodingException e) {
			this.setErrorCode(5000);
		} catch (GeneralSecurityException ge) {
			this.setErrorCode(6000);
		} catch (IOException io) {
			this.setErrorCode(7000);
		}

	}

	/**
	 * This method creates a new bucket using Google's Cloud Storage API without
	 * taking location as part of the parameters.
	 * 
	 * @param pathToCredentials
	 * @param projectName
	 * @param bucketNameToCreate
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public void createBucket(String pathToCredentials, String projectName,
			String bucketNameToCreate) throws IOException,
			GeneralSecurityException {

		GoogleCredential credentials = null;
		try {
			credentials = GoogleCredential.fromStream(
					new FileInputStream(pathToCredentials)).createScoped(
					Collections.singleton(STORAGE_SCOPE));
		} catch (FileNotFoundException f) {
			this.setErrorCode(101);
		}

		String bucketName = bucketNameToCreate;

		try {

			String uri = "https://www.googleapis.com/storage/v1/b?project="
					+ projectName;

			HttpTransport httpTransport = GoogleNetHttpTransport
					.newTrustedTransport();

			HttpRequestFactory requestFactory = httpTransport
					.createRequestFactory(credentials);

			GenericUrl url = new GenericUrl(uri);

			HttpContent content = new JsonHttpContent(new JacksonFactory(),
					createJSONObject(bucketName));

			com.google.api.client.http.HttpRequest request = requestFactory
					.buildPostRequest(url, content);

			HttpResponse response = request.execute();

			int rs = response.getStatusCode();

			System.out.println("The bucket has been created.\n");
		} catch (UnsupportedEncodingException e) {
			this.setErrorCode(5000);
		} catch (GeneralSecurityException ge) {
			this.setErrorCode(6000);
		} catch (IOException io) {
			this.setErrorCode(7000);
		}

	}

	public HashMap<String, String> createJSONObject(String bucketName,
			String location) {

		HashMap<String, String> jb = new HashMap<String, String>();

		jb.put("name", bucketName);
		jb.put("location", location);

		return jb;

	}

	public HashMap<String, String> createJSONObject(String bucketName) {
		HashMap<String, String> jb = new HashMap<String, String>();

		jb.put("name", bucketName);

		return jb;

	}

	public void uploadToBucket(String bucketName, String projectName) {
		GoogleCredential credentials = null;
		try {
			credentials = GoogleCredential.fromStream(
					new FileInputStream(pathToCredentials)).createScoped(
					Collections.singleton(STORAGE_SCOPE));
		} catch (FileNotFoundException f) {
			this.setErrorCode(101);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			String uri = "https://www.googleapis.com/upload/storage/v1/b/"
					+ bucketName + "/o?uploadType=media&name=" + projectName;

			System.out.println(uri);

			HttpTransport httpTransport = GoogleNetHttpTransport
					.newTrustedTransport();

			HttpRequestFactory requestFactory = httpTransport
					.createRequestFactory(credentials);

			GenericUrl url = new GenericUrl(uri);

			System.out.println(url);

			HttpContent content = new JsonHttpContent(new JacksonFactory(),
					createJSONObject(bucketName));

			com.google.api.client.http.HttpRequest request = requestFactory
					.buildPostRequest(url, content);

			HttpResponse response = request.execute();

			System.out.println("The bucket has been created.\n");
		} catch (UnsupportedEncodingException e) {
			this.setErrorCode(5000);
		} catch (GeneralSecurityException ge) {
			this.setErrorCode(6000);
		} catch (IOException io) {
			this.setErrorCode(7000);
		}
	}

	/**
	 * This method will retrieve all the buckets in a given project using
	 * project ID as identifier
	 * 
	 * @param bucketName
	 */
	public void retrieveBuckets(String projectName) {

		ArrayList<String> buckets = new ArrayList<String>();

		GoogleCredential credentials = null;

		try {
			credentials = GoogleCredential.fromStream(
					new FileInputStream(pathToCredentials)).createScoped(
					Collections.singleton(STORAGE_SCOPE));
		} catch (FileNotFoundException f) {
			this.setErrorCode(101);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			String uri = "https://www.googleapis.com/storage/v1/b/?project="
					+ projectName;

			HttpTransport httpTransport = GoogleNetHttpTransport
					.newTrustedTransport();

			HttpRequestFactory requestFactory = httpTransport
					.createRequestFactory(credentials);

			GenericUrl url = new GenericUrl(uri);

			com.google.api.client.http.HttpRequest request = requestFactory
					.buildGetRequest(url);

			HttpResponse response = request.execute();

			String content = response.parseAsString();

			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(content);
			JsonObject kindObject = root.getAsJsonObject();

			JsonArray bucketArray = kindObject.getAsJsonArray("items");

			Gson gson = new GsonBuilder().serializeNulls().create();

			for (int i = 0; i < bucketArray.size(); i++) {
				JsonObject blob_object = bucketArray.get(i).getAsJsonObject();

				String name = blob_object.get("name").getAsString();

				buckets.add(name);

				System.out.println(name);
			}

			System.out.println("Bucket:" + content);

		} catch (UnsupportedEncodingException e) {
			this.setErrorCode(5000);
		} catch (GeneralSecurityException ge) {
			this.setErrorCode(6000);
		} catch (IOException io) {
			this.setErrorCode(7000);
		}
	}

	/**
	 * This method will retrieve a selected bucket and returns its content The
	 * method will read the response from the server and processed it into a
	 * Json Object. It will then get the array of item and turn it into a
	 * JsonArray. In each JsonArray, each item from the bucket can be retrieved.
	 * 
	 * @param bucketName
	 */
	public void retrieveBlobs(String bucketName) {

		ArrayList<String> buckets = new ArrayList<String>();

		GoogleCredential credentials = null;

		try {
			credentials = GoogleCredential.fromStream(
					new FileInputStream(pathToCredentials)).createScoped(
					Collections.singleton(STORAGE_SCOPE));
		} catch (FileNotFoundException f) {
			this.setErrorCode(101);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			String uri = "https://www.googleapis.com/storage/v1/b/"
					+ bucketName + "/o";

			HttpTransport httpTransport = GoogleNetHttpTransport
					.newTrustedTransport();

			HttpRequestFactory requestFactory = httpTransport
					.createRequestFactory(credentials);

			GenericUrl url = new GenericUrl(uri);

			com.google.api.client.http.HttpRequest request = requestFactory
					.buildGetRequest(url);

			HttpResponse response = request.execute();

			String content = response.parseAsString();

			JsonParser jp = new JsonParser();
			
			JsonElement root = jp.parse(content);
			
			JsonObject kindObject = root.getAsJsonObject();

			JsonArray bucketArray = kindObject.getAsJsonArray("items");

			Gson gson = new GsonBuilder().serializeNulls().create();

			for (int i = 0; i < bucketArray.size(); i++) {
				JsonObject blob_object = bucketArray.get(i).getAsJsonObject();

				String name = blob_object.get("name").getAsString();

				buckets.add(name);

				System.out.println(name);
			}

			System.out.println("Bucket:" + content);

		} catch (UnsupportedEncodingException e) {
			this.setErrorCode(5000);
		} catch (GeneralSecurityException ge) {
			this.setErrorCode(6000);
		} catch (IOException io) {
			this.setErrorCode(7000);
		}
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

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
