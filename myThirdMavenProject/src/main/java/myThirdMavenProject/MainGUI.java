package myThirdMavenProject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is the Graphical User Interface of a Cloud Console Java Program
 * It is Java Program that provides some functionality of the Google Cloud Console
 * @author espinajohn
 *
 */
public class MainGUI extends Application {

	// Fields
	Alert alert = new Alert(AlertType.INFORMATION);
	
	// Fields for Main HomePage View
	Stage primaryStage = new Stage();
	String title = "Google Cloud Console";
	Text welcome = new Text("Choose from the following options:");
	Button create = new Button("Create a Bucket");
	Button upload = new Button("Upload Files");
	Button merge = new Button("Merge Buckets");
	Button split = new Button("Split a Bucket");

	// Fields for Create View Page
	TextField projectId = new TextField();
	TextField credentialPath = new TextField();
	TextField bucketName = new TextField();
	Text projectText = new Text("Project ID");
	Text credentialText = new Text("Credential Path");
	Text bucketText = new Text("Bucket Name");
	Button createButton = new Button("Create Bucket");
	Text methodText = new Text("Choose the method for creating the bucket");
	Text locationText = new Text("Location");
	TextField location = new TextField();

	// Fields for Upload View Page
	Text blobFileText = new Text("File to Upload");
	TextField blobFile = new TextField();
	Button uploadButton = new Button("Upload Blob");
	Text bucketNameText = new Text("Choose a Bucket");
	TextField projectIdUpload = new TextField();
	TextField credentialPathUpload = new TextField();
	Text projectTextUpload = new Text("Project ID");
	Text credentialTextUpload = new Text("Credential Path");
	Button nextButton = new Button("Next");
	Text blobNameText = new Text("Name this Blob");
	TextField blobName = new TextField();
	Text methodToUploadText = new Text("Select a methhod to Upload");
	ComboBox methodToUpload = new ComboBox();
	ComboBox menu = new ComboBox();

	// Fields for Merge View
	TextField projectIdMerge = new TextField();
	TextField credentialPathMerge = new TextField();
	Text projectTextMerge = new Text("Project ID");
	Text credentialTextMerge = new Text("Credential Path");
	Button nextButtonMerge = new Button("Next");
	Text chooseMultiBucketText = new Text(
			"Choose the Buckets you want to merge:");
	Button mergeButton = new Button("Merge");
	Text bucketNameMergeText = new Text(
			"ATTENTION. This action will delete selected buckets \nand merge it automatically to a new Bucket. \nPlease enter the name of the new Bucket that will be created");
	TextField bucketNameMerge = new TextField();
	Text mergeOptionText = new Text ("Choose method for merging");
	CheckBox[] checkboxes = null;

	// Fields for Split View
	TextField projectIdSplit = new TextField();
	TextField credentialPathSplit = new TextField();
	Text projectTextSplit = new Text("Project ID");
	Text credentialTextSplit = new Text("Credential Path");
	Button nextButtonSplit = new Button("Next");
	Text bucketToSplitMenuText = new Text("Choose a Bucket to Split");
	ComboBox bucketToSplitMenu = new ComboBox();
	Button nextButtonSplit2 = new Button("Next");
	Button nextButtonSplit3 = new Button("Next");
	Text chooseMultiBlobText = new Text(
			"Select the Blobs you want to move to move first");
	Button splitButton = new Button("Move and Split");
	Text newBucketAText = new Text(
			"Name of the First New Bucket where the Blobs will be moved");
	Text newBucketBText = new Text("Name of the Second New Bucket");
	TextField newBucketA = new TextField();
	TextField newBucketB = new TextField();
	Text methodToSplitText = new Text("Select a methhod to Split");
	ComboBox methodToSplit = new ComboBox();
	CheckBox[] blobCheckBoxes = null;

	/**
	 * This method is called to go back to the Home Page.
	 * Called after every tasked is completed.
	 * @param primaryStage
	 */
	protected void backToHome(Stage primaryStage) {
		try {
			start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {

		alert.setTitle("Google Cloud Assistant");

		// main view
		Group homeRoot = new Group();
		Scene homeView = new Scene(homeRoot, 500, 500);

		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(15, 15, 15, 15));
		box.getChildren().add(welcome);
		box.getChildren().add(create);
		create.setPrefWidth(150);
		box.getChildren().add(upload);
		upload.setPrefWidth(150);
		box.getChildren().add(merge);
		merge.setPrefWidth(150);
		box.getChildren().add(split);
		split.setPrefWidth(150);
		box.setMargin(welcome, new Insets(10, 10, 10, 10));
		box.setMargin(create, new Insets(10, 10, 10, 10));
		box.setMargin(upload, new Insets(10, 10, 10, 10));
		box.setMargin(merge, new Insets(10, 10, 10, 10));
		box.setMargin(split, new Insets(10, 10, 10, 10));

		homeRoot.getChildren().add(box);

		/*
		 * This block of code implements the new View related to the Create functionality
		 * This view is called when a the Create Button is clicked
		 */
		create.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				
				final ComboBox methodOptions = GuiHelper.methodOptions();

				final ComboBox locationOptions = GuiHelper.locationOptions();

				Group creationRoot = GuiHelper.addScene(primaryStage);

				GridPane creationBox = GuiHelper.addGridpane();

				GuiHelper.askCredentials(creationBox, projectText, projectId,
						credentialText, credentialPath);

				creationBox.setConstraints(bucketText, 0, 4);
				creationBox.getChildren().add(bucketText);
				creationBox.setConstraints(bucketName, 1, 4);
				creationBox.getChildren().add(bucketName);
				creationBox.setConstraints(locationText, 0, 6);
				creationBox.getChildren().add(locationText);
				creationBox.setConstraints(locationOptions, 1, 6);
				creationBox.getChildren().add(locationOptions);
				creationBox.setConstraints(methodText, 0, 8);
				creationBox.getChildren().add(methodText);
				creationBox.setConstraints(methodOptions, 1, 8);
				creationBox.getChildren().add(methodOptions);
				creationBox.setConstraints(createButton, 0, 12);
				creationBox.getChildren().add(createButton);

				creationRoot.getChildren().add(creationBox);

				createButton.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent event) {

						int errorCode = 0;
						// Get the input from the user
						String projectInput = projectId.getText();
						String credentialsPathInput = credentialPath.getText();
						String bucketNameInput = bucketName.getText();
						String locationInput = null;
						String methodInput = null;
						try {
							locationInput = locationOptions.getSelectionModel()
									.getSelectedItem().toString();
							methodInput = methodOptions.getSelectionModel()
									.getSelectedItem().toString();
						} catch (java.lang.NullPointerException je) {
							errorCode = 1000;
							ErrorChecker.check(errorCode, alert);

						}

						if (methodInput.contains("API")) {

							CloudManagerAPI cloudManagerAPI = new CloudManagerAPI(
									projectInput, credentialsPathInput);
							cloudManagerAPI.retrieveBlobs(bucketNameInput);

							try {

								if (location.equals("")) {
									cloudManagerAPI.createBucket(
											credentialsPathInput, projectInput,
											bucketNameInput);
									errorCode = cloudManagerAPI.getErrorCode();

								} else {
									cloudManagerAPI.createBucket(
											credentialsPathInput, projectInput,
											bucketNameInput, locationInput);
									errorCode = cloudManagerAPI.getErrorCode();
									
									

								}

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (GeneralSecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (errorCode != 0) {

								ErrorChecker.check(errorCode, alert);

							} else {

								alert.setContentText("New Bucket( "
										+ bucketNameInput
										+ " ) Created Successfully");
								alert.showAndWait();

								backToHome(primaryStage);
							}

						}

						else {

							// Create a new Cloud Manager
							CloudManagerSDK cloudManagerSDK = new CloudManagerSDK(
									projectInput, credentialsPathInput);

							try {

								if (location.equals("")) {
									cloudManagerSDK
											.createBucket(bucketNameInput);
									errorCode = Integer
											.parseInt(cloudManagerSDK
													.getErrorCode());
								} else {
									cloudManagerSDK.createBucket(
											bucketNameInput, locationInput);
									errorCode = Integer
											.parseInt(cloudManagerSDK
													.getErrorCode());

								}

							} catch (StorageException e) {

								errorCode = e.getCode();
								e.printStackTrace();

							}

							if (errorCode != 0) {

								ErrorChecker.check(errorCode, alert);

							} else {

								alert.setContentText("New Bucket( "
										+ bucketNameInput
										+ " ) Created Successfully");
								alert.showAndWait();

								backToHome(primaryStage);
							}

						}
						bucketNameInput = null;
					}

				});
			}
		});

		
		/*
		 * This block of code implements the new View related to the Upload functionality
		 * This view is called when a the Upload Button is clicked
		 */
		upload.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				final ComboBox methodToUpload = GuiHelper.methodOptions();

				Group uploadRoot = GuiHelper.addScene(primaryStage);

				GridPane creationBox = GuiHelper.addGridpane();

				GuiHelper.askCredentials(creationBox, projectTextUpload,
						projectIdUpload, credentialTextUpload,
						credentialPathUpload);

				creationBox.setConstraints(nextButton, 0, 8);

				creationBox.getChildren().add(nextButton);

				uploadRoot.getChildren().add(creationBox);

				nextButton.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent event) {

						String projectInput = projectIdUpload.getText();
						String credentialsPathInput = credentialPathUpload
								.getText();

						if ((projectInput.equals(""))
								|| (credentialsPathInput.equals(""))) {
							alert.setContentText("Please enter details");
							alert.showAndWait();

						} else {

							final CloudManagerSDK cloudManagerSDK = new CloudManagerSDK(
									projectInput, credentialsPathInput);

							Group uploadRoot2 = GuiHelper
									.addScene(primaryStage);

							try {
								menu = BucketsLister
										.listBucketsInMenu(cloudManagerSDK);

							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}

							GridPane creationBox = new GridPane();

							creationBox.setConstraints(blobFileText, 0, 4);
							creationBox.getChildren().add(blobFileText);
							creationBox.setConstraints(blobFile, 1, 4);
							creationBox.getChildren().add(blobFile);
							creationBox.setConstraints(bucketNameText, 0, 6);
							creationBox.getChildren().add(bucketNameText);
							creationBox.setConstraints(menu, 1, 6);
							creationBox.getChildren().add(menu);
							creationBox.setConstraints(blobNameText, 0, 8);
							creationBox.getChildren().add(blobNameText);
							creationBox.setConstraints(blobName, 1, 8);
							creationBox.getChildren().add(blobName);
							creationBox.setConstraints(methodToUploadText, 0,
									10);
							creationBox.getChildren().add(methodToUploadText);
							creationBox.setConstraints(methodToUpload, 1, 10);
							creationBox.getChildren().add(methodToUpload);
							creationBox.setConstraints(uploadButton, 0, 12);
							creationBox.getChildren().add(uploadButton);

							uploadRoot2.getChildren().add(creationBox);

							uploadButton
									.setOnAction(new EventHandler<ActionEvent>() {

										public void handle(ActionEvent event) {

											int errorCode = 0;

											String bucketName = menu
													.getSelectionModel()
													.getSelectedItem()
													.toString();

											System.out.println(bucketName);

											String path = blobFile.getText();

											String blobNameInput = blobName
													.getText();

											try {
												cloudManagerSDK.uploadBlob(
														cloudManagerSDK,
														bucketName,
														blobNameInput, path);

											} catch (StorageException e) {
												errorCode = e.getCode();
												e.printStackTrace();

											} catch (FileNotFoundException e) {
												e.printStackTrace();

											} catch (IOException e) {
												e.printStackTrace();

											}

											if (errorCode != 0) {

												ErrorChecker.check(errorCode,
														alert);

											} else if (errorCode == 0) {

												alert.setContentText("Blob( "
														+ blobNameInput
														+ " ) Uploaded Successfully to "
														+ bucketName);
												alert.showAndWait();

												backToHome(primaryStage);
											}
										}

									});

						}

					}
				});

			}

		});
		
		
		/*
		 * This block of code implements the new View related to the Merge functionality
		 * This view is called when a the Merge Button is clicked
		 */
		merge.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				final ComboBox mergeOptionsMenu = GuiHelper.methodOptions();
				
				Group mergeRoot = GuiHelper.addScene(primaryStage);

				GridPane creationBox = GuiHelper.addGridpane();

				GuiHelper.askCredentials(creationBox, projectTextMerge,
						projectIdMerge, credentialTextMerge,
						credentialPathMerge);

				creationBox.setConstraints(nextButtonMerge, 0, 8);

				creationBox.getChildren().add(nextButtonMerge);

				mergeRoot.getChildren().add(creationBox);

				nextButtonMerge.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent event) {

						String projectInput = projectIdMerge.getText();
						String credentialsPathInput = credentialPathMerge
								.getText();

						if ((projectInput.equals(""))
								|| (credentialsPathInput.equals(""))) {
							alert.setContentText("Please enter details");
							alert.showAndWait();

						}

						else {

							final CloudManagerSDK cloudManagerSDK = new CloudManagerSDK(
									projectInput, credentialsPathInput);

							Group mergeRoot2 = GuiHelper.addScene(primaryStage);

							try {
								checkboxes = BucketsLister
										.listBucketsWithCheckBox(cloudManagerSDK);

							} catch (IOException e) {
								e.printStackTrace();
							}

							VBox buttonHolder = new VBox();
							buttonHolder.getChildren().add(mergeButton);
							buttonHolder.setAlignment(Pos.BASELINE_RIGHT);
							buttonHolder.setPadding(new Insets(30, 30, 30, 5));

							VBox bucketList = new VBox();
							bucketList.setPadding(new Insets(15, 15, 15, 15));
							bucketList.setSpacing(10.0);
							bucketList.getChildren().add(chooseMultiBucketText);
							bucketList.getChildren().addAll(checkboxes);
							bucketList.getChildren().add(mergeOptionText);
							bucketList.getChildren().add(mergeOptionsMenu);
							bucketList.getChildren().add(bucketNameMergeText);
							bucketList.getChildren().add(bucketNameMerge);
							bucketList.getChildren().add(buttonHolder);

							mergeRoot2.getChildren().addAll(bucketList);

							mergeButton
									.setOnAction(new EventHandler<ActionEvent>() {

										public void handle(ActionEvent event) {

											String bucketName = bucketNameMerge
													.getText();

											int errorCode = 0;

											ArrayList<String> buckets = new ArrayList<String>();

											for (int i = 0; i < checkboxes.length; i++) {
												if (checkboxes[i].isSelected()) {
													buckets.add(checkboxes[i]
															.getText());
												}
											}

											try {
												cloudManagerSDK.mergeBuckets(
														buckets, bucketName);
												errorCode = Integer
														.parseInt(cloudManagerSDK
																.getErrorCode());

											} catch (StorageException e) {
												errorCode = e.getCode();
												e.printStackTrace();

											}

											if (errorCode != 0) {

												ErrorChecker.check(errorCode,
														alert);

											} else {

												alert.setContentText("Selected buckets merged successfully");
												alert.showAndWait();

												backToHome(primaryStage);
											}

										}

									});
						}

					}

				});

			}

		});

		
		/*
		 * This block of code implements the new View related to the Split functionality
		 * This view is called when a the Split Button is clicked
		 */
		split.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				final ComboBox methodToSplit = GuiHelper.methodOptions();
				
				Group splitRoot = GuiHelper.addScene(primaryStage);

				GridPane creationBox = GuiHelper.addGridpane();

				GuiHelper.askCredentials(creationBox, projectTextSplit,
						projectIdSplit, credentialTextSplit,
						credentialPathSplit);
				
		
				creationBox.setConstraints(nextButtonSplit, 0, 8);

				creationBox.getChildren().add(nextButtonSplit);

				splitRoot.getChildren().add(creationBox);

				nextButtonSplit.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent event) {

						String projectInput = projectIdSplit.getText();
						String credentialsPathInput = credentialPathSplit
								.getText();

						if ((projectInput.equals(""))
								|| (credentialsPathInput.equals(""))) {
							alert.setContentText("Please enter details");
							alert.showAndWait();

						}

						else {

							final CloudManagerSDK cloudManagerSDK = new CloudManagerSDK(
									projectInput, credentialsPathInput);

							Group splitRoot2 = GuiHelper.addScene(primaryStage);

							try {

								bucketToSplitMenu = BucketsLister
										.listBucketsInMenu(cloudManagerSDK);

							} catch (IOException e1) {

								e1.printStackTrace();
							}

							GridPane creationBox = GuiHelper.addGridpane();

							creationBox.setConstraints(bucketToSplitMenuText,
									0, 6);

							creationBox.getChildren()
									.add(bucketToSplitMenuText);

							creationBox.setConstraints(bucketToSplitMenu, 1, 6);

							creationBox.getChildren().add(bucketToSplitMenu);
							
							creationBox.setConstraints(methodToSplitText, 0, 8);

							creationBox.getChildren().add(methodToSplitText);
							
							creationBox.setConstraints(methodToSplit, 1, 8);

							creationBox.getChildren().add(methodToSplit);

							creationBox.setConstraints(nextButtonSplit2, 0, 10);

							creationBox.getChildren().add(nextButtonSplit2);

							splitRoot2.getChildren().add(creationBox);

							nextButtonSplit2
									.setOnAction(new EventHandler<ActionEvent>() {

										public void handle(ActionEvent event) {

											final String bucketToSplit = bucketToSplitMenu
													.getSelectionModel()
													.getSelectedItem()
													.toString();

											Group splitRoot3 = GuiHelper
													.addScene(primaryStage);

											try {

												blobCheckBoxes = BucketsLister
														.listBlobsWithCheckBox(
																cloudManagerSDK,
																bucketToSplit);

											} catch (IOException e) {
												e.printStackTrace();
											}

											VBox buttonHolder = new VBox();
											buttonHolder.getChildren().add(
													splitButton);
											buttonHolder
													.setAlignment(Pos.BASELINE_RIGHT);
											buttonHolder.setPadding(new Insets(
													30, 30, 30, 5));

											VBox blobList = new VBox();
											blobList.setPadding(new Insets(15,
													15, 15, 15));
											blobList.setSpacing(10.0);
											blobList.getChildren().add(
													chooseMultiBlobText);
											blobList.getChildren().addAll(
													blobCheckBoxes);
											blobList.getChildren().add(
													newBucketAText);
											blobList.getChildren().add(
													newBucketA);
											blobList.getChildren().add(
													newBucketBText);
											blobList.getChildren().add(
													newBucketB);
											blobList.getChildren().add(
													buttonHolder);

											splitRoot3.getChildren().addAll(
													blobList);

											splitButton
													.setOnAction(new EventHandler<ActionEvent>() {

														public void handle(
																ActionEvent event) {

															String newBucketAInput = newBucketA
																	.getText();
															String newBucketBInput = newBucketB
																	.getText();

															ArrayList<String> blobsToMoveA = new ArrayList<String>();
															ArrayList<String> blobsToMoveB = new ArrayList<String>();

															int errorCode = 0;

															for (int i = 0; i < blobCheckBoxes.length; i++) {
																if (blobCheckBoxes[i]
																		.isSelected()) {
																	blobsToMoveA
																			.add(blobCheckBoxes[i]
																					.getText());
																}
															}

															for (int i = 0; i < blobCheckBoxes.length; i++) {
																if (!blobCheckBoxes[i]
																		.isSelected()) {
																	blobsToMoveB
																			.add(blobCheckBoxes[i]
																					.getText());
																}
															}

															try {
																cloudManagerSDK
																		.splitAndMove(
																				blobsToMoveA,
																				blobsToMoveB,
																				newBucketAInput,
																				newBucketBInput,
																				bucketToSplit);

															} catch (StorageException e) {
																errorCode = e
																		.getCode();
																e.printStackTrace();

															}

															if (errorCode != 0) {

																ErrorChecker
																		.check(errorCode,
																				alert);

															} else {

																alert.setContentText(bucketToSplit
																		+ " has been succesfully splitted to "
																		+ newBucketAInput
																		+ " and "
																		+ newBucketBInput);
																alert.showAndWait();

																backToHome(primaryStage);
															}

														}

													});

										}

									});

						}

					}

				});

			}

		});

		primaryStage.setScene(homeView);
		primaryStage.setTitle("Google Cloud Console");
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);

	}

}
