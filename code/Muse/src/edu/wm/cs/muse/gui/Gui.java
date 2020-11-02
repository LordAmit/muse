package edu.wm.cs.muse.gui;


import java.io.File;
import java.io.FileWriter;

import org.eclipse.text.edits.MalformedTreeException;

import edu.wm.cs.muse.Muse;
import edu.wm.cs.muse.dataleak.support.Arguments;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



// Development notes: Transitions will probably be done with stages. Each frame/activity
// will be a new stage. See how it works here: https://youtu.be/7LxWQIDOzyE?t=464

// Top left of screen is 0,0


/*
 * This is the class for the main GUI window.
 * @author Phil Watkins
 */
public class Gui extends Application {
	Pane root;
	Button makeConfigBtn;
	Button oldConfigBtn;
	Text titleText;
	Stage window;
	
	

    // public static void main(String[] args) {
    //     launch(args);
    // }
    
    /*
     * This method has to be implemented because Gui "extends"
     * Application (for JavaFX functionality).
     */
    @Override
    public void start(Stage primaryStage) {
    	window = primaryStage;
        window.setTitle("μSE"); // window name
        
        goToTitleScene(primaryStage);

        window.setResizable(false);
        window.show();
    }
    
    /////////////////////////////////////TITLE SCENE CODE///////////////////////////////////////////////
    /*
     * Creates the title scene. 
     * Has buttons for making a config and using an existing config
     * Also has mSE title
     */
    private void goToTitleScene(Stage stage) {
    	BackgroundFill background_fill = new BackgroundFill(Color.GRAY,  
                CornerRadii.EMPTY, Insets.EMPTY); 
		
		// create Background 
		Background background = new Background(background_fill); 
        

        createButtons(stage); // initializes 2 buttons and gives each a handler for running code on being clicked
        createTitleText();
        root = new Pane();
        root.getChildren().addAll(makeConfigBtn, oldConfigBtn, titleText); // can add individual elements too
        root.setBackground(background); // solid gray
        window.setScene(new Scene(root, 600, 600)); // layout, width, height
    }
    
    /**
     * Creates the Buttons for:
     * 1. Running muse with an existing configuration file  (oldConfigBtn)
     * 2. Creating a new configuration file in a new window (makeConfigBtn)
     */
    private void createButtons(Stage stage) {
    	makeConfigBtn = new Button("Create a new configuration");
    	oldConfigBtn = new Button("Use an existing configuration");
//    	makeConfigBtn.setText("Create a new configuration");
//    	oldConfigBtn.setText("Use an existing configuration");
    	
    	// Attach an event handler to the button for running muse with an existing config file
    	makeConfigBtn.setOnAction(new EventHandler<ActionEvent>() {
   		 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("You pressed the button that will bring up the window for"
                		+ " creating a new configuration file when implemented.");
                goToConfigCreation(stage);
            }
        });
    	makeConfigBtn.setLayoutX(600 * .6);
    	makeConfigBtn.setLayoutY(600 * .8);
    	
    	// Attach an event handler to the button for creating a new config.properties file
    	oldConfigBtn.setOnAction(new EventHandler<ActionEvent>() {
    		 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("You pressed the button that will ask which existing configuration"
                		+ " you want to run muse with.");
                
                FileChooser fileChooser = new FileChooser();
                String filePath = fileChooser.showOpenDialog(window).getAbsolutePath();
                String[] run = {filePath};

                try {
                	Arguments.extractArguments(run[0]);
					new Muse().runMuse(run);
				} catch (MalformedTreeException | org.eclipse.jface.text.BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    	
    	//presents an error (currently works off a button)
    	//TODO: Implement to be called when a real error is detected
    	//goToErrorScene(oldConfigBtn);
    	
    	
    	oldConfigBtn.setLayoutX(600 * .1);
    	oldConfigBtn.setLayoutY(600 * .8);
    	
    }
    
    public void createTitleText() {
        titleText = new Text("μSE");
        titleText.setLayoutX(225);
        titleText.setLayoutY(600/4);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFont(new Font(80));
        titleText.setFill(Color.GREEN);
    }
    
    
    //////////////////////////////////CONFIG CREATION CODE//////////////////////////////////////////////////
    /**
     * Takes the user to the config file creation form window.
     */
    private void goToConfigCreation(Stage stage) {
    	GridPane configLayout = new GridPane();
        FlowPane leftbanner = new FlowPane();
//        leftbanner.setPrefWidth(30); //200?
//        String bgStyle = "-fx-background-color: lightgreen;"
//           + "-fx-background-radius: 0%;"
//           + "-fx-background-inset: 5px;";
//           leftbanner.setStyle(bgStyle);
//
//           configLayout.add(leftbanner, 0, 0, 1, 1);
           configLayout.add(createGridPane(stage), 1, 0, 1, 1);
           Scene scene = new Scene(configLayout, 750, 655);
           window.setScene(scene);
    }
    
    /**
     * Sets up the GridPane layout for the form
     * @return The created layout, which is then added to the scene.
     */
    private GridPane createGridPane(Stage stage) {
    	
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Text txt = new Text("Generate a Configuration File");
        txt.setFont(Font.font("Dialog", FontWeight.BOLD, 12));

        grid.add(txt, 0, 0, 1, 1);
        grid.add(new Separator(), 0, 1, 3, 1);
        
        TextField config_name_textfield = new TextField();
        grid.add(new Label("Configuration Name:"), 0, 2, 1, 1);
        grid.add(config_name_textfield, 1, 2, 1, 1);
        
        //!!new!!
        //libs4ast directory chooser
        TextField lib4ast_path_textfield = new TextField();
        grid.add(new Label("lib4ast Path:"), 0, 3, 1, 1);
        grid.add(lib4ast_path_textfield, 1, 3, 1, 1);
        Button libs4astbrowse = new Button("Browse...");
        grid.add(libs4astbrowse, 2, 3, 1, 1);
        MuseDirectoryChooser(libs4astbrowse,lib4ast_path_textfield,stage);
        
        
        
        grid.add(new Label("Operator:"), 0, 4, 1, 1);
        ComboBox<String> operatorSelections = new ComboBox<>();
        operatorSelections.setPrefWidth(400);
        
        operatorSelections.getItems().addAll("SCOPESOURCE", "SCOPESINK", "TAINTSOURCE",
        									 "TAINTSINK", "COMPLEXREACHABILITY", "REACHABILITY");
        grid.add(operatorSelections, 1, 4, 1, 1);
        
        
        CheckBox mutate_checkbox = new CheckBox("Mutate");
        grid.add(mutate_checkbox, 0, 7, 3, 1);
        grid.add(new Separator(), 0, 6, 3, 1);
        
        //!!new!!
        //app_source directory chooser
        TextField app_src_textfield = new TextField();
        grid.add(new Label("App src Location:"), 0, 5, 1, 1);
        grid.add(app_src_textfield, 1, 5, 1, 1);
        Button appbrowse = new Button("Browse...");
        grid.add(appbrowse, 2, 5, 1, 1);
        MuseDirectoryChooser(appbrowse,app_src_textfield,stage);
        
        //!!new!!
        //destination directory chooser
        TextField destination_folder_textfield = new TextField();
        grid.add(new Label("Destination Folder:"), 0, 8, 1, 1);
        grid.add(destination_folder_textfield, 1, 8, 1, 1);
        Button destbrowse = new Button("Browse...");
        grid.add(destbrowse, 2, 8, 1, 1);
        MuseDirectoryChooser(destbrowse,destination_folder_textfield,stage);
        
        
        
        TextField app_name_textfield = new TextField();
        grid.add(new Label("App Name:"), 0, 9, 1, 1);
        grid.add(app_name_textfield, 1, 9, 1, 1);

        
		destination_folder_textfield.setDisable(!mutate_checkbox.isSelected());
        app_name_textfield.setDisable(!mutate_checkbox.isSelected());
        
        mutate_checkbox.setOnAction(new EventHandler<ActionEvent>() {
    		 
            @Override
            public void handle(ActionEvent event) {
        		destination_folder_textfield.setDisable(!mutate_checkbox.isSelected());
        		app_name_textfield.setDisable(!mutate_checkbox.isSelected());
        		updateBorder(destination_folder_textfield, true);
        		updateBorder(app_name_textfield, true);
            }
        });

        
        grid.add(new Separator(), 0, 10, 3, 1);

        CheckBox log_checkbox = new CheckBox("Log Analyze");
        grid.add(log_checkbox, 0, 11, 3, 1);

        TextField insertion_log_path_textfield = new TextField();
        grid.add(new Label("\tInsertion Log Path:"), 0, 12, 1, 1);
        grid.add(insertion_log_path_textfield, 1, 12, 1, 1);
        
        TextField execution_log_path_textfield = new TextField();
        grid.add(new Label("\tExecution Log Path:"), 0, 13, 1, 1);
        grid.add(execution_log_path_textfield, 1, 13, 1, 1);
        
        insertion_log_path_textfield.setDisable(!log_checkbox.isSelected());
		execution_log_path_textfield.setDisable(!log_checkbox.isSelected()); 
        
        log_checkbox.setOnAction(new EventHandler<ActionEvent>() {
      		 
            @Override
            public void handle(ActionEvent event) {
        		insertion_log_path_textfield.setDisable(!log_checkbox.isSelected());
        		execution_log_path_textfield.setDisable(!log_checkbox.isSelected()); 
        		updateBorder(insertion_log_path_textfield, true);
        		updateBorder(execution_log_path_textfield, true);
            }
        });

        grid.add(new Separator(), 0, 14, 3, 1);

        CheckBox custom_data_leak_checkbox = new CheckBox("Use Custom Data Leak");
        grid.add(custom_data_leak_checkbox, 0, 15, 3, 1);

        TextField source_string_textfield = new TextField();
        grid.add(new Label("\tSource String:"), 0, 16, 1, 1);
        grid.add(source_string_textfield, 1, 16, 1, 1);
        
        TextField sink_string_textfield = new TextField();
        grid.add(new Label("\tSink String:"), 0, 17, 1, 1);
        grid.add(sink_string_textfield, 1, 17, 1, 1);
        
        TextField vardec_string_textfield = new TextField();
        grid.add(new Label("\tvarDec String:"), 0, 18, 1, 1);
        grid.add(vardec_string_textfield, 1, 18, 1, 1);

        grid.add(new Label("Once created, configurations can be reused\n"
           + "(see Help for details)"),1, 19, 1, 1);
        
        source_string_textfield.setDisable(!custom_data_leak_checkbox.isSelected());
        sink_string_textfield.setDisable(!custom_data_leak_checkbox.isSelected()); 
        vardec_string_textfield.setDisable(!custom_data_leak_checkbox.isSelected()); 
        
		custom_data_leak_checkbox.setOnAction(new EventHandler<ActionEvent>() {
      		 
            @Override
            public void handle(ActionEvent event) {
            	source_string_textfield.setDisable(!custom_data_leak_checkbox.isSelected());
                sink_string_textfield.setDisable(!custom_data_leak_checkbox.isSelected()); 
                vardec_string_textfield.setDisable(!custom_data_leak_checkbox.isSelected()); 
                updateBorder(source_string_textfield, true);
        		updateBorder(sink_string_textfield, true);
        		updateBorder(vardec_string_textfield, true);
            }
        });


        grid.add(new Separator(), 0, 20, 3, 1);
        
        Button finish = new Button("Finish");
        finish.setOnAction(new EventHandler<ActionEvent>() {
   		 
            @Override
            public void handle(ActionEvent event) {
            	
            	if (validInputs(config_name_textfield, lib4ast_path_textfield, app_src_textfield, operatorSelections, mutate_checkbox ,app_name_textfield, destination_folder_textfield, log_checkbox, insertion_log_path_textfield, execution_log_path_textfield, custom_data_leak_checkbox, source_string_textfield, sink_string_textfield, vardec_string_textfield))
            	{
            		GenerateConfig.generateConfig(config_name_textfield.getText(), lib4ast_path_textfield.getText(), app_src_textfield.getText(), operatorSelections.getValue(), mutate_checkbox.isSelected() ,app_name_textfield.getText(), destination_folder_textfield.getText(), log_checkbox.isSelected(), insertion_log_path_textfield.getText(), execution_log_path_textfield.getText(), custom_data_leak_checkbox.isSelected(), source_string_textfield.getText(), sink_string_textfield.getText(), vardec_string_textfield.getText());
            	}
            }
        });
        
        Button returnTitleButton = new Button("Back");
        returnTitleButton.setOnAction(new EventHandler<ActionEvent>() {
   		 
            @Override
            public void handle(ActionEvent anevent) {
            	goToTitleScene(stage);
            	System.out.println("'Back' selected. Returning to title.");
            	
            	}
        });
        
        
        Button runMuseButton = new Button("Run Muse");
        runMuseButton.setOnAction(new EventHandler<ActionEvent>() {
   		 
            @Override
            public void handle(ActionEvent runEvent) {
            	goToProgressScene(stage);
            	System.out.println("'Run Muse' selected. Proceeding to Progress Scene.");
            	
            	}
        });


        FlowPane fp = new FlowPane(Orientation.HORIZONTAL, 10, 10);
        fp.setAlignment(Pos.CENTER_RIGHT);
        fp.getChildren().addAll(
           returnTitleButton,
           finish,
//         new Button("Run Muse"), 	// new Muse().runMuse(args);
         runMuseButton,
           new Button("Help"));		// explanations of what each config parameter is, what operators do, etc?
        grid.add(fp, 0, 21, 3, 1);

        return grid;
     }
    
    /*
     * On a button press open dir_choser
     * sets text-field to the folder
     */
    private void MuseDirectoryChooser(Button SearchButton,TextField textfield, Stage stage) {
   	 DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
        SearchButton.setOnAction(e -> {
       	 File selectedDirectory = directoryChooser.showDialog(stage);
       	 	try {
       	 	String directoryName = selectedDirectory.getAbsolutePath();
            textfield.setText(directoryName);
       	 	} catch(Exception dup) {
       	 		System.out.print("No folder selected.");
       	 	}
           
        });
   }
    
    /////////////////////////////////////PROGRESS/RUNTIME SCENE CODE////////////////////////////////////////
    
    /*
     * Changes the scene to the one with a progress bar.
     * Gives information as Muse runs.
     * TODO: Implement rest of layout 
     * TODO: Sync progress bar progress to Muse progress
     */
    private void goToProgressScene(Stage stage) {   	
    	
    	//create VBox object
    	VBox loading = new VBox(20);
    	//standard dimensions we have been using
        loading.setMaxWidth(600); 
        loading.setMaxHeight(600);
        loading.setPadding(new Insets(10, 50, 50, 50));
        
        
        //Creating a Text object 
        Text runningText = new Text(); 
        runningText.setText("μSE is running");
        runningText.setFont(Font.font("Inconsolata ",  FontWeight.BOLD, 30));
        //add element to VBox
        loading.getChildren().add(runningText);
        
        //create progress bar and add
        final ProgressBar museProgressBar = new ProgressBar();
        museProgressBar.prefWidthProperty().bind(root.widthProperty().subtract(20));
        museProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        //add element to VBox
        loading.getChildren().add(museProgressBar);
        
        //Creating a separator (separates bar from text)
        Separator sep = new Separator();
        sep.setMaxWidth(80);
        sep.setHalignment(HPos.CENTER);
        //add element to VBox
        loading.getChildren().add(2, sep);
        
        //create text for muse output to be printed and add
        TextArea museRuntimeText = new TextArea();
        //dummy text for now
        museRuntimeText.setText("Muse progress toasts can print here:\n");
        museRuntimeText.setFont(Font.font("Courier New",  20));
        museRuntimeText.setWrapText(true);
        //add element to VBox
        loading.getChildren().add(museRuntimeText); //add the text to pane
            
        //create dummy button for testing text and add
        Button testToastButton = new Button("Get toast");
        loading.getChildren().add(testToastButton);
        
        //create dummy button for testing finish state
        Button proceedFinishButton = new Button("Proceed");
        loading.getChildren().add(proceedFinishButton);
        
        //create  button for returning to start
        Button returnStart = new Button("Cancel");
        loading.getChildren().add(returnStart);
        
        //!!!!!!!!!adding action handlers for buttons!!!!!!!!!!!
        //for our test button, updates text on click for now
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//testText.setText("Accepted");
            	museRuntimeText.appendText("Button Press detected-- Adding toast.\n");
            	
                event.consume();
             
            } 
        };
        testToastButton.setOnAction(buttonHandler);
        
      //for our proceed button, go to finish activity
        EventHandler<ActionEvent> finishButtonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//call finish activity
            	museRuntimeText.appendText("\nFinish button selected!\nInvoking goToSuccessfulRunScene()"
            			+ "\nBringing TextArea to new function\n");
            	//takes the TextArea with runtime string into our final function stage
            	//this lets us display the text to the user for potential use after running muse
            	goToSuccessfulRunScene(stage,museRuntimeText);
                event.consume();
             
            } 
        };
        proceedFinishButton.setOnAction(finishButtonHandler);
        
      //for our cancel button, return to start menu
        EventHandler<ActionEvent> returnStartHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//call finish activity
            	museRuntimeText.appendText("\nCancel button selected! Returning to start.\n");
            	goToTitleScene(stage);
                event.consume();
             
            } 
        };
        returnStart.setOnAction(returnStartHandler);
      //!!!!!!!!!end of action handlers for buttons!!!!!!!!!!!
  
        BorderPane root = new BorderPane(loading);
        Scene scene = new Scene(root);
  
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setScene(scene);
        stage.show();
        
    }
    
    /*
     * Changes scene to final screen that shows where log file is.
     * This screen is only visited if muse ran successfully.
     * If there was an error, it will take the user to the
     * error scene instead
     */
    private void goToSuccessfulRunScene(Stage stage,TextArea runTimeInfoText) {
    	
    	//create VBox object
    	VBox success = new VBox(20);
    	//standard dimensions we have been using
    	success.setMaxWidth(600); 
    	success.setMaxHeight(600);
    	success.setPadding(new Insets(10, 50, 50, 50));
        
        //Creating a Text object 
        Text successText = new Text(); 
        successText.setText("μSE has successfully completed its run!");
        successText.setFont(Font.font("Inconsolata ",  FontWeight.BOLD, 30));
        //add element to VBox
        success.getChildren().add(successText);
        
        //Creating a Text object 
        Text infoText = new Text(); 
        infoText.setText("Runtime text log:");
        infoText.setFont(Font.font("Inconsolata ", 20));
        //add element to VBox
        success.getChildren().add(infoText);
        
        
        BorderPane root = new BorderPane(success);
        Scene scene = new Scene(root);    
        
        //add TextArea with SAME text from Progress Scene
        success.getChildren().add(runTimeInfoText); //add the text to pane
  
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setScene(scene);
        stage.show();
    	
    }
    
    /*
     * If muse crashes or there is an error, the user is taken to this scene.
     * this scene gives information about why muse crashed.
     * Might give suggestions about what could have gone wrong.
     */
    private void goToErrorScene(Button button) {
    	Alert a = new Alert(AlertType.NONE);
    	
    	EventHandler<ActionEvent> errorEvent = new 
                EventHandler<ActionEvent>() { 
					   public void handle(ActionEvent e){ 
					       // set alert type 
					       a.setAlertType(AlertType.ERROR); 
					       // set content text 
					       a.setContentText("error Dialog");        
					       //a.setButtonType(ButtonType.OK);
					       // show the dialog 
					       a.show(); 
					   } 
					};
						
					
		//currently sets alert on button press of passed button
		//TODO: Change to activate on detected errors (filepaths etc.)
		button.setOnAction(errorEvent);
    }
	 	 
    /**
     * Checks all input fields and returns true if no required fields are empty and marks the fields that are not
     * @param config_name_textfield
     * @param lib4ast_path_textfield
     * @param app_src_textfield
     * @param operatorSelections
     * @param mutate_checkbox
     * @param app_name_textfield
     * @param destination_folder_textfield
     * @param log_checkbox
     * @param insertion_log_path_textfield
     * @param execution_log_path_textfield
     * @param custom_data_leak_checkbox
     * @param source_string_textfield
     * @param sink_string_textfield
     * @param vardec_string_textfield
     * @return true if all required fields contain text
     */
    private boolean validInputs(TextField config_name_textfield, TextField lib4ast_path_textfield, TextField app_src_textfield, ComboBox<String> operatorSelections, CheckBox mutate_checkbox , TextField app_name_textfield, TextField destination_folder_textfield, CheckBox log_checkbox, TextField insertion_log_path_textfield, TextField execution_log_path_textfield, CheckBox custom_data_leak_checkbox, TextField source_string_textfield, TextField sink_string_textfield, TextField vardec_string_textfield)
    {
    	boolean valid = true;	
    	if (config_name_textfield.getText().equals("")) {
    		updateBorder(config_name_textfield, false);
    		valid = false; }
    	else
    		updateBorder(config_name_textfield, true);
    	if (lib4ast_path_textfield.getText().equals("")) {
        	updateBorder(lib4ast_path_textfield, false);
        	valid = false; }
    	else
        	updateBorder(lib4ast_path_textfield, true);
    	if (app_src_textfield.getText().equals("")) {
    		updateBorder(app_src_textfield, false);
    		valid = false; }
    	else
    		updateBorder(app_src_textfield, true);
    	if (operatorSelections.getValue() == null) {
    		updateBorder(operatorSelections, false);
    		valid = false; }
    	else
    		updateBorder(operatorSelections, true);
    	if (mutate_checkbox.isSelected()) {        	
    		if (app_name_textfield.getText().equals("")) {
        		updateBorder(app_name_textfield, false);
        		valid = false; }
    		else
        		updateBorder(app_name_textfield, true);
        	if (destination_folder_textfield.getText().equals("")) {
        		updateBorder(destination_folder_textfield, false);
        		valid = false; } 
        	else
        		updateBorder(destination_folder_textfield, true); }
    	else
    	{
    		updateBorder(app_name_textfield, true);
    		updateBorder(destination_folder_textfield, true); }
    	if (log_checkbox.isSelected())
    	{
    	if (insertion_log_path_textfield.getText().equals("")) {
    			updateBorder(insertion_log_path_textfield, false);
        		valid = false; }
    		else
    			updateBorder(insertion_log_path_textfield, true);
        	if (execution_log_path_textfield.getText().equals("")) {
            	updateBorder(execution_log_path_textfield, false);
        		valid = false; }
        	else
            	updateBorder(execution_log_path_textfield, true); }
    	else
    	{
			updateBorder(insertion_log_path_textfield, true);
        	updateBorder(execution_log_path_textfield, true); }
    	if (custom_data_leak_checkbox.isSelected())
    	{
    		if (source_string_textfield.getText().equals("")) {
        		updateBorder(source_string_textfield, false);
        		valid = false; }
    		else
        		updateBorder(source_string_textfield, true);
        	
        	if (sink_string_textfield.getText().equals("")) {
            	updateBorder(sink_string_textfield, false);
        		valid = false; }
        	else
            	updateBorder(sink_string_textfield, true);
        	if (vardec_string_textfield.getText().equals("")) {
            	updateBorder(vardec_string_textfield, false);
        		valid = false; }
        	else
            	updateBorder(vardec_string_textfield, true); }
    	else {
    		updateBorder(source_string_textfield, true);
        	updateBorder(sink_string_textfield, true);
        	updateBorder(vardec_string_textfield, true); }
    	
    	return valid;
    	}
    
    private void updateBorder(TextField tf, boolean valid)
    {
    	if (valid)
    		tf.setStyle("-fx-border-color: red ; -fx-border-width: 0px ;");
    	else
    		tf.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    }
    
    private void updateBorder(ComboBox<String> cb, boolean valid)
    {
    	if (valid) {
    		cb.setStyle("-fx-border-color: red ; -fx-border-width: 0px ;"); }
    	else {
    		cb.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;"); }
    }
	 
}
