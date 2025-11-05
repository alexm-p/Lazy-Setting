package io.github.alexm_p.lazysetting.app;

import io.github.alexm_p.lazysetting.core.TrainingBookManager;
import io.github.alexm_p.lazysetting.model.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LazySettingApp extends Application {
    private Canvas canvas;
    private BoardRenderer renderer;
    private Board currentBoard;
    private Label statusLabel;
    private CheckBox showLabelsCheck;
    private CheckBox showGridCheck;
    private Slider zoomSlider;
    private double dragStartX, dragStartY;
    private double translateX = 0, translateY = 0;
    private double scale = 1.0;

    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Initialize canvas and renderer
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setStyle("-fx-border-color: #333; -fx-border-width: 2;");
        renderer = new BoardRenderer(canvas);

        // Top toolbar
        HBox toolbar = createToolbar(primaryStage);
        root.setTop(toolbar);

        // Center canvas
        StackPane canvasPane = new StackPane(canvas);
        canvasPane.setStyle("-fx-background-color: #2a2a2a;");
        root.setCenter(canvasPane);


        // Bottom status bar
        HBox statusBar = createStatusBar();
        root.setBottom(statusBar);

        // Right panel for controls
        VBox rightPanel = createRightPanel();
        root.setRight(rightPanel);

        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setTitle("Lazy Setting - Board Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create a demo board
        createDemoBoard();
        updateDisplay();

        canvasPane.setOnMousePressed(e -> {
            dragStartX = e.getX();
            dragStartY = e.getY();
        });

        canvasPane.setOnMouseDragged(e -> {
            double deltaX = e.getX() - dragStartX;
            double deltaY = e.getY() - dragStartY;
            translateX += deltaX;
            translateY += deltaY;
            dragStartX = e.getX();
            dragStartY = e.getY();
            updateDisplay();
        });

        canvas.setOnScroll(e -> {
            double oldScale = scale;
            double zoomFactor = 1.05;
            if (e.getDeltaY() < 0) {
                scale /= zoomFactor;
            } else {
                scale *= zoomFactor;
            }
            double scaleRatio = scale / oldScale;

            // Focus point is the center of the canvas
            double focusX = (double) CANVAS_WIDTH / 2;
            double focusY = (double) CANVAS_HEIGHT / 2;

            translateX = (translateX - focusX) * scaleRatio + focusX;
            translateY = (translateY - focusY) * scaleRatio + focusY;

            renderer.setScale(scale);
            updateDisplay();
        });
    }

    private HBox createToolbar(Stage stage) {
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setStyle("-fx-background-color: #1e1e1e;");

        Button loadButton = new Button("Load Board");
        loadButton.setOnAction(e -> loadBoardFile(stage));

        Button demoButton = new Button("Demo Board");
        demoButton.setOnAction(e -> {
            createDemoBoard();
            updateDisplay();
        });

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            currentBoard = null;
            renderer.clear();
            statusLabel.setText("Canvas cleared");
        });

        toolbar.getChildren().addAll(loadButton, demoButton, clearButton);
        return toolbar;
    }

    private VBox createRightPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #252525;");
        panel.setPrefWidth(250);

        Label title = new Label("Display Options");
        title.setFont(Font.font(16));
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        showLabelsCheck = new CheckBox("Show Hold Info");
        showLabelsCheck.setSelected(true);
        showLabelsCheck.setStyle("-fx-text-fill: white;");
        showLabelsCheck.setOnAction(e -> updateDisplay());

        showGridCheck = new CheckBox("Show Grid");
        showGridCheck.setSelected(true);
        showGridCheck.setStyle("-fx-text-fill: white;");
        showGridCheck.setOnAction(e -> updateDisplay());

        Label zoomLabel = new Label("Zoom:");
        zoomLabel.setStyle("-fx-text-fill: white;");

        zoomSlider = new Slider(0.5, 2.0, 1.0);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setMajorTickUnit(0.5);
        zoomSlider.valueProperty().addListener((obs, old, newVal) -> {
            renderer.setScale(newVal.doubleValue());
            updateDisplay();
        });

        Separator sep1 = new Separator();

        Label legendLabel = new Label("Difficulty Legend:");
        legendLabel.setFont(Font.font(14));
        legendLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        VBox legend = createLegend();

        panel.getChildren().addAll(
                title, showLabelsCheck, showGridCheck,
                zoomLabel, zoomSlider,
                sep1, legendLabel, legend
        );

        return panel;
    }

    private VBox createLegend() {
        VBox legend = new VBox(8);
        legend.setPadding(new Insets(5));

        String[] ranges = {"0-2: Easy", "2-4: Moderate", "4-6: Hard",
                "6-8: Very Hard", "8-10: Extreme"};
        Color[] colors = {Color.GREEN, Color.YELLOW, Color.ORANGE,
                Color.RED, Color.DARKRED};

        for (int i = 0; i < ranges.length; i++) {
            HBox item = new HBox(10);
            item.setAlignment(Pos.CENTER_LEFT);

            Canvas colorBox = new Canvas(20, 20);
            var gc = colorBox.getGraphicsContext2D();
            gc.setFill(colors[i]);
            gc.fillRect(0, 0, 20, 20);
            gc.setStroke(Color.WHITE);
            gc.strokeRect(0, 0, 20, 20);

            Label label = new Label(ranges[i]);
            label.setStyle("-fx-text-fill: white;");

            item.getChildren().addAll(colorBox, label);
            legend.getChildren().add(item);
        }

        return legend;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.setStyle("-fx-background-color: #1e1e1e;");

        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #aaa;");

        statusBar.getChildren().add(statusLabel);
        return statusBar;
    }

    private void createDemoBoard() {
        List<Hold> holds = new ArrayList<>();

        // Create a variety of holds with different types and difficulties
        holds.add(new Hold(HoldType.JUG, 1.0, new Pose(100, 500, 0)));
        holds.add(new Hold(HoldType.EDGE, 3.5, new Pose(200, 450, 45)));
        holds.add(new Hold(HoldType.CRIMP, 6.0, new Pose(300, 400, 90)));
        holds.add(new Hold(HoldType.SLOPER, 7.5, new Pose(400, 350, 135)));
        holds.add(new Hold(HoldType.PINCH, 5.0, new Pose(500, 300, 180)));
        holds.add(new Hold(HoldType.POCKET, 4.0, new Pose(600, 250, 225)));
        holds.add(new Hold(HoldType.JUG, 2.0, new Pose(150, 350, 270)));
        holds.add(new Hold(HoldType.CRIMP, 8.5, new Pose(350, 200, 315)));
        holds.add(new Hold(HoldType.EDGE, 9.0, new Pose(550, 150, 0)));
        holds.add(new Hold(HoldType.SLOPER, 3.0, new Pose(250, 250, 60)));

        currentBoard = new Board(holds);
        statusLabel.setText("Demo board loaded: " + holds.size() + " holds");
    }

    private void loadBoardFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Board File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Board Files", "*.board", "*.json")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                currentBoard = TrainingBookManager.loadBoard(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateDisplay() {
        if (currentBoard == null) {
            renderer.clear();
            return;
        }

        renderer.render(currentBoard, showGridCheck.isSelected(), showLabelsCheck.isSelected(), translateX, translateY);

        statusLabel.setText("Displaying " + currentBoard.holds().size() +
                " holds (zoom: " + String.format("%.1f", renderer.getScale()) + "x)");
    }

    public static void main(String[] args) {
        launch(args);
    }
}