package io.github.joedegiovanni.stuff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class StuffDataService {

    public static final String DEFAULT_STUFF_FILE_NAME = "data/my-stuff.json";
    public static final String STUFF_FILE_NAME = "data/my-stuff.json";

    public final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public void resetToDefaultStuff() throws IOException {
        Files.copy(Path.of(DEFAULT_STUFF_FILE_NAME), Path.of(STUFF_FILE_NAME), StandardCopyOption.REPLACE_EXISTING);
    }
    
    public JsonElement readJson() {
        try (final var inputStream = getStuffResourceUrl().openStream()) {
            String json = new String(inputStream.readAllBytes());
            return gson.fromJson(json, JsonElement.class);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load stuff", e);
        }
    }

    public void saveJson(Stuff object) throws FileNotFoundException {
        try (final var printWriter = new PrintWriter(getStuffResourceUrl().getPath())) {
            printWriter.print(gson.toJson(object));
            printWriter.flush();
        }
    }

    private URL getStuffResourceUrl() {
        try {
            return new File(STUFF_FILE_NAME).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

}
