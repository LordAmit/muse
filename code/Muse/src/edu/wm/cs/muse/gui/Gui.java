package edu.wm.cs.muse.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
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
	

    public static void main(String[] args) {
        launch(args);
    }
    
    /*
     * This method has to be implemented because Gui "extends"
     * Application (for JavaFX functionality).
     */
    @Override
    public void start(Stage primaryStage) {
    	window = primaryStage;
        window.setTitle("μSE"); // window name
        
        goToTitleScene();

        window.setResizable(false);
        window.show();
    }
    
    /////////////////////////////////////TITLE SCENE CODE///////////////////////////////////////////////
    /*
     * Creates the title scene. 
     * Has buttons for making a config and using an existing config
     * Also has mSE title
     */
    private void goToTitleScene() {
    	BackgroundFill background_fill = new BackgroundFill(Color.GRAY,  
                CornerRadii.EMPTY, Insets.EMPTY); 
		
		// create Background 
		Background background = new Background(background_fill); 
        

        createButtons(); // initializes 2 buttons and gives each a handler for running code on being clicked
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
    private void createButtons() {
    	makeConfigBtn = new Button();
    	oldConfigBtn = new Button();
    	makeConfigBtn.setText("Create a new configuration");
    	oldConfigBtn.setText("Use an existing configuration");
    	
    	// Attach an event handler to the button for running muse with an existing config file
    	makeConfigBtn.setOnAction(new EventHandler<ActionEvent>() {
   		 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("You pressed the button that will bring up the window for"
                		+ " creating a new configuration file when implemented.");
                goToConfigCreation();
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
                
                // TODO:File chooser for browse buttons
                FileChooser fileChooser = new FileChooser();
                fileChooser.showOpenDialog(window);
            }
        });
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
    private void goToConfigCreation() {
    	GridPane configLayout = new GridPane();
        FlowPane leftbanner = new FlowPane();
        leftbanner.setPrefWidth(50); //200?
        String bgStyle = "-fx-background-color: lightgreen;"
           + "-fx-background-radius: 0%;"
           + "-fx-background-inset: 5px;";
           leftbanner.setStyle(bgStyle);

           configLayout.add(leftbanner, 0, 0, 1, 1);
           configLayout.add(createGridPane(), 1, 0, 1, 1);
           Scene scene = new Scene(configLayout, 750, 575);
           window.setScene(scene);
    }
    
    /**
     * Sets up the GridPane layout for the form
     * @return The created layout, which is then added to the scene.
     */
    private GridPane createGridPane() {
//    	Button back, next, finish, cancel, help = new Button();
    	Button cancel = new Button("Cancel");
    	cancel.setOnAction(new EventHandler<ActionEvent>() {
   		 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("cancelled operation");
            }
        });
    	
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Text txt = new Text("Required Inputs");
        txt.setFont(Font.font("Dialog", FontWeight.BOLD, 12));

        grid.add(txt, 0, 0, 1, 1);
        grid.add(new Separator(), 0, 1, 3, 1);

        grid.add(new Label("App Name:"), 0, 2, 1, 1);
        grid.add(new TextField(), 1, 2, 1, 1);

        grid.add(new Label("App src Location:"), 0, 3, 1, 1);
        grid.add(new TextField(), 1, 3, 1, 1);
        grid.add(new Button("Browse..."), 2, 3, 1, 1);

        grid.add(new Label("Destination Folder:"), 0, 4, 1, 1);
        grid.add(new TextField(), 1, 4, 1, 1);
        grid.add(new Button("Browse..."), 2, 4, 1, 1);


        grid.add(new Label("Operator:"), 0, 5, 1, 1);
        ComboBox<String> operatorSelections = new ComboBox<>();
        operatorSelections.setPrefWidth(400);
        
        operatorSelections.getItems().addAll("SCOPESOURCE", "SCOPESINK", "TAINTSOURCE",
        									 "TAINTSINK", "COMPLEXREACHABILITY", "REACHABILITY");
        
        grid.add(operatorSelections, 1, 5, 1, 1);
        
        grid.add(new Separator(), 0, 6, 3, 1);

        grid.add(new CheckBox("Log Analyze"), 0, 7, 3, 1);

        grid.add(new Label("\tInsertion Log Path:"), 0, 9, 1, 1);
        grid.add(new TextField(), 1, 9, 1, 1);
        
        grid.add(new Label("\tExecution Log Path:"), 0, 10, 1, 1);
        grid.add(new TextField(), 1, 10, 1, 1);

        grid.add(new Separator(), 0, 11, 3, 1);

        grid.add(new CheckBox("Use Custom Data Leak"), 0, 12, 3, 1);

        grid.add(new Label("\tSource String:"), 0, 13, 1, 1);
        grid.add(new TextField(), 1, 13, 1, 1);
        
        grid.add(new Label("\tSink String:"), 0, 14, 1, 1);
        grid.add(new TextField(), 1, 14, 1, 1);
        
        grid.add(new Label("\tvarDec String:"), 0, 15, 1, 1);
        grid.add(new TextField(), 1, 15, 1, 1);

        grid.add(new Label("Once created, configurations can be reused\n"
           + "(see Help for details)"),1, 16, 1, 1);


        grid.add(new Separator(), 0, 17, 3, 1);

        FlowPane fp = new FlowPane(Orientation.HORIZONTAL, 10, 10);
        fp.setAlignment(Pos.CENTER_RIGHT);
        fp.getChildren().addAll(
           new Button("< Back"),
           new Button("Next >"),
           new Button("Finish"),
           new Button("Cancel"),
           new Button("Help"));
        grid.add(fp, 0, 18, 3, 1);

        return grid;
     }
    
    
    /////////////////////////////////////PROGRESS/RUNTIME SCENE CODE////////////////////////////////////////
    
    /*
     * Changes the scene to the one with a progress bar.
     * Gives information as μSE runs.
     */
    private void goToProgressScene() {
//    	HBox progressSceneLayout = new HBox();
//    	Scene progressScene = new Scene(progressSceneLayout, width, height);
    	
    }
    
    /*
     * Changes scene to final screen that shows where log file is.
     * This screen is only visited if muse ran successfully.
     * If there was an error, it will take the user to the
     * error scene instead
     */
    private void goToSuccessfulRunScene() {
    	
    }
    
    /*
     * If muse crashes or there is an error, the user is taken to this scene.
     * this scene gives information about why muse crashed.
     * Might give suggestions about what could have gone wrong.
     */
    private void goToErrorScene() {
    	
    }
	 	 
	 
}