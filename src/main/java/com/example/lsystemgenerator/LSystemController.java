package com.example.lsystemgenerator;

import com.example.lsystemgenerator.util.LSystem;
import com.example.lsystemgenerator.util.Turtle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LSystemController implements Initializable {
    @FXML
    private Pane lsystemViewportPane;
    @FXML
    private TextField axiomInput;
    @FXML
    private ListView<Pair<TextField, TextField>> rulesListView;
    @FXML
    private TextField iterationsInput;
    @FXML
    private TextField angleInput;

    private LSystem currentLSystem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rulesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Pair<TextField, TextField> item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    hbox.getChildren().addAll(item.getKey(), item.getValue());
                    setGraphic(hbox);
                }
            }
        });

        rulesListView.getItems().add(new Pair<>(new TextField(), new TextField()));
    }

    @FXML
    public void onAddRuleButtonClick() {
        rulesListView.getItems().add(new Pair<>(new TextField(), new TextField()));
    }

    public void onGenerateButtonClick(ActionEvent actionEvent) {
        System.out.println("Generate button clicked");

        // get the axiom, rule, and iterations from the input fields
        String axiom = axiomInput.getText();
        Map<String, String> rules = rulesListView.getItems().stream()
                .filter(pair -> !pair.getKey().getText().isBlank() && !pair.getValue().getText().isBlank())
                .map(pair -> Map.entry(pair.getKey().getText(), pair.getValue().getText()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        int iterations = Integer.parseInt(iterationsInput.getText());
        double angle = Double.parseDouble(angleInput.getText());

        // create an LSystem object with the axiom, rule, and iterations
        LSystem lsystem = new LSystem(axiom, rules, iterations, angle);
        this.currentLSystem = lsystem;

        // print LSystem
        System.out.println(currentLSystem);

        display3D();
    }

    private void display3D() {
        double angle = currentLSystem.angle();
        String generatedString = currentLSystem.generate();

        // Create a turtle
        Turtle turtle = new Turtle();

        // Create a group to hold the 3D model
        Group group = new Group();

        TriangleMesh mesh = new TriangleMesh();
        mesh.getTexCoords().addAll(0, 0);
        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(new PhongMaterial(Color.GREEN));

        List<Point3D> points = new ArrayList<>();


        // Interpret the generated string as turtle commands
        for (char c : generatedString.toCharArray()) {
            if (c == 'F') {
                // Move the turtle forward
                Point3D oldPosition = turtle.getPosition();
                turtle.forward(1);
                Point3D newPosition = turtle.getPosition();

                // Add the points to the list
                points.add(oldPosition);
                points.add(newPosition);
            } else if (c == '+') {
                // Turn the turtle
                turtle.turn(angle);
            } else if (c == '-') {
                // Turn the turtle
                turtle.turn(-angle);
            } else if (c == '[') {
                // Push the turtle's state
                turtle.push();
            } else if (c == ']') {
                // Pop the turtle's state
                turtle.pop();
            } else if (c == '^') {
                // Pitch the turtle
                turtle.pitch(angle);
            } else if (c == 'v') {
                // Pitch the turtle
                turtle.pitch(-angle);
            } else if (c == '<') {
                // Roll the turtle
                turtle.roll(angle);
            } else if (c == '>') {
                // Roll the turtle
                turtle.roll(-angle);
            }
        }

        // Add the points to the TriangleMesh
        for (Point3D point : points) {
            mesh.getPoints().addAll((float) point.getX(), (float) point.getY(), (float) point.getZ());
        }

        // Connect the points with triangles
        for (int i = 0; i < points.size() - 2; i++) {
            mesh.getFaces().addAll(i, 0, i + 2, 0, i + 1, 0);
        }

        // Add the MeshView to the group
        group.getChildren().add(meshView);

        // Create a camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-100);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setFieldOfView(45);

        // Create a subscene
        SubScene subScene = new SubScene(group, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTBLUE);
        subScene.setCamera(camera);


        // Add the subscene to the pane
        lsystemViewportPane.getChildren().clear();
        lsystemViewportPane.getChildren().add(subScene);

        double mouseSpeed = 0.1;
        double[] mouseOld = new double[2];

        subScene.setOnMousePressed(mouseEvent -> {
            mouseOld[0] = mouseEvent.getX();
            mouseOld[1] = mouseEvent.getY();
        });

        subScene.setOnMouseDragged(mouseEvent -> {
            double mouseDeltaX = mouseEvent.getX() - mouseOld[0];
            double mouseDeltaY = mouseEvent.getY() - mouseOld[1];

            camera.setTranslateX(camera.getTranslateX() - mouseSpeed * mouseDeltaX);
            camera.setTranslateY(camera.getTranslateY() - mouseSpeed * mouseDeltaY);

            mouseOld[0] = mouseEvent.getX();
            mouseOld[1] = mouseEvent.getY();
        });

        subScene.setOnScroll(scrollEvent -> {
            double scrollDeltaY = scrollEvent.getDeltaY();

            camera.setTranslateZ(camera.getTranslateZ() + scrollDeltaY);
        });

        subScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) {
                camera.setTranslateZ(camera.getTranslateZ() + 1);
            } else if (keyEvent.getCode() == KeyCode.DOWN) {
                camera.setTranslateZ(camera.getTranslateZ() - 1);
            } else if (keyEvent.getCode() == KeyCode.LEFT) {
                camera.setTranslateX(camera.getTranslateX() - 1);
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                camera.setTranslateX(camera.getTranslateX() + 1);
            }
        });
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
        System.out.println("Save button clicked (WIP)");

        // TODO: Save the LSystem to a file
    }

    public void onLoadButtonClick(ActionEvent actionEvent) {
        System.out.println("Load button clicked (WIP)");

        // TODO: Load an LSystem from a file
    }

    public void onQuitButtonClick(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onRemoveRuleButtonClick(ActionEvent actionEvent) {
        System.out.println("Remove rule button clicked");

        // check a rule is selected
        if (rulesListView.getSelectionModel().getSelectedItem() != null) {
            // remove the selected rule
            rulesListView.getItems().remove(rulesListView.getSelectionModel().getSelectedItem());
        }
    }

    public void onExportButtonClick(ActionEvent actionEvent) {
        // TODO: Export the LSystem as an OBJ file
    }
}
