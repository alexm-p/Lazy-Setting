package io.github.alexm_p.lazysetting.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.alexm_p.lazysetting.model.Board;
import io.github.alexm_p.lazysetting.problem.Problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TrainingBookManager {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void saveBoard(Board board, String filename) throws IOException {
        String json = gson.toJson(board);
        Files.writeString(Path.of(filename), json);
    }
    public static Board loadBoard(String filename) throws IOException {
        Path path = getResourceAsPath(filename);
        String json = Files.readString(path);
        return gson.fromJson(json, Board.class);
    }

    public static Board loadBoard(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, Board.class);
        }
    }

    // TODO: compleat
    public static void saveProblems(List<Problem> problems, String filename) throws Exception {

    }

    // TODO: compleat
    public static Board loadProblems(String filename) throws Exception {
        return null;
    }

    private static Path getResourceAsPath(String resourceName) throws IOException {
        ClassLoader classLoader = TrainingBookManager.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceName);

        if (resourceUrl == null) {
            throw new FileNotFoundException("Resource not found: " + resourceName);
        }

        try {
            return Path.of(resourceUrl.toURI());
        } catch (URISyntaxException e) {
            throw new IOException("Invalid resource URI: " + resourceName, e);
        }
    }
}