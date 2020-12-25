package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller extends Application {

    // Area for displaying tree
    public Pane treeDisplay = new Pane();
    // Main layout pane
    BorderPane main = new BorderPane();
    // Row of nodes at the top
    HBox topRow = new HBox(),
    // Insert/Search/Delete, input box, confirm button
    userInputs = new HBox(),
    // Degree of tree, generate button, clear button
    treeCommands = new HBox(),
    // Height, number of vertices
    bottomRow = new HBox();

    // Drop down menu items
    String[] actions = {"Insert", "Find", "Delete"};
    // Drop down menu
    ChoiceBox<String> actionSelector = new ChoiceBox<>(FXCollections.observableArrayList(actions));

    // Field in which the user will enter numbers
    TextField userNumberInput = new TextField("Enter a number");

    // Press this button to execute task
    Button confirmButton = new Button("Confirm"), clearButton = new Button("Clear");

    // Text that displays the degree of the tree
    Text degreeText = new Text("DEGREE : 3"),
    // Text that displays the height of the tree
    heightText = new Text("HEIGHT : 0"),
    // Text that displays the vertices of the tree
    verticesText = new Text("VERTICES : 0");

    // Fade animation for the tree
    FadeTransition fade = new FadeTransition(Duration.millis(900), treeDisplay);

    // Tree object on which we apply all the operations
    GLOperations<Integer> tree = new GLOperations<>(treeDisplay);
    // Handler for all mouse events
    EventHandler<MouseEvent> mouseClickHandler = mouseEvent -> {
        // Clear the text field before typing something new
        if (mouseEvent.getSource() == userNumberInput)
            userNumberInput.setText("");
            // Insert a new number
        else if (mouseEvent.getSource() == confirmButton && actionSelector.getValue().equals("Insert")) {
            // Add to the tree
            tree.add(Integer.parseInt(userNumberInput.getText()));
            // Clear existing nodes
            tree.clearNodes();
            // Recreate the display with the newly added node
            tree.callCreateDisplay();
            // Update information
            updateInfoTexts();
            // Apply the fade animation to the display
            fadeAnimation();
        }
        // Clear the screen
        else if (mouseEvent.getSource() == clearButton) {
//            tree.showList();
            treeDisplay.getChildren().clear();
            updateInfoTexts();
            tree = new GLOperations<>(treeDisplay);
        }
        // Find a number
        else if (mouseEvent.getSource() == confirmButton && actionSelector.getValue().equals("Find"))
            tree.find(userNumberInput.getText());
        // Delete a number
        else if (mouseEvent.getSource() == confirmButton && actionSelector.getValue().equals("Delete")) {
            tree.remove(Integer.parseInt(userNumberInput.getText()));
            tree.clearNodes();
            tree.callCreateDisplay();
            updateInfoTexts();
            fadeAnimation();
        }
    };

    // Method to update height and number of vertices if required
    void updateInfoTexts() {
        heightText.setText("HEIGHT : " + tree.getHeight());
        verticesText.setText("VERTICES : " + tree.getListSize());
    }

    // Method to cause a fade effect
    void fadeAnimation() {
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    @Override
    public void start(Stage stage) {

        setPaneTheme(topRow, 100.0, Color.LIGHTBLUE);
        setPaneTheme(bottomRow, 50.0, Color.LIGHTBLUE);
        setPaneTheme(userInputs, 50.0, Color.DEEPSKYBLUE);
        setPaneTheme(treeCommands, 50.0, Color.DEEPSKYBLUE);

        topRow.setSpacing(20.0);
        topRow.setAlignment(Pos.CENTER);
        topRow.getChildren().addAll(userInputs, treeCommands);

        actionSelector.setValue("Insert");

        degreeText.setStyle("-fx-font: 16 franklingothicmedium");

        userInputs.setSpacing(20.0);
        treeCommands.setSpacing(20.0);

        userNumberInput.setOnMouseClicked(mouseClickHandler);
        confirmButton.setOnMouseClicked(mouseClickHandler);
        clearButton.setOnMouseClicked(mouseClickHandler);

        userInputs.getChildren().addAll(actionSelector, userNumberInput, confirmButton);
        treeCommands.getChildren().addAll(degreeText, clearButton);

        heightText.setStyle("-fx-font: 24 franklingothicmedium; -fx-font-weight: 600; -fx-font-style: italic");
        verticesText.setStyle("-fx-font: 24 franklingothicmedium; -fx-font-weight: 600; -fx-font-style: italic");

        heightText.setFill(Color.RED);
        verticesText.setFill(Color.RED);

        bottomRow.setSpacing(30.0);
        bottomRow.setAlignment(Pos.CENTER);
        bottomRow.getChildren().addAll(heightText, verticesText);

        main.setTop(topRow);
        main.setCenter(treeDisplay);
        main.setBottom(bottomRow);

        Scene scene = new Scene(main, 800, 650);

        stage.setScene(scene);
        stage.setTitle("B-Tree Visualiser");
        stage.show();

    }

    // Helper method to set a general theme to a pane
    void setPaneTheme(Pane pane, double height, Paint paint) {
        pane.setBackground(
                new Background(
                        new BackgroundFill(
                                paint,
                                null,
                                null
                        )
                )
        );
        pane.setPadding(new Insets(20.0));
        pane.setMinHeight(height);
    }

}
