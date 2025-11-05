package io.github.alexm_p.lazysetting.app;

import io.github.alexm_p.lazysetting.core.TrainingBook;
import io.github.alexm_p.lazysetting.core.TrainingBookManager;
import io.github.alexm_p.lazysetting.model.Board;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class LazySettingApp extends Application {

    private TrainingBook tb;
    private BoardRenderer br;
    private Board myBoard;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        myBoard = TrainingBookManager.loadBoard("tension_board2_spray_layout.json");
        tb = new TrainingBook(myBoard, new ArrayList<>());

    }
}