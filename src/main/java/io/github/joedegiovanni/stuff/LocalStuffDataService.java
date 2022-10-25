package io.github.joedegiovanni.stuff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static io.github.joedegiovanni.stuff.Sneaky.wrap;

public class LocalStuffDataService implements IStuffDataService {

    public static final String DEFAULT_STUFF_FILE_NAME = "data/default-stuff.json";
    public static final String STUFF_FILE_NAME = "data/my-stuff.json";

    public final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void resetToDefaultStuff() {
        wrap(() -> Files.copy(Path.of(DEFAULT_STUFF_FILE_NAME), Path.of(STUFF_FILE_NAME), StandardCopyOption.REPLACE_EXISTING));
    }

    public JsonElement readJson() {
        try (final var inputStream = getStuffResourceUrl().openStream()) {
            String json = new String(inputStream.readAllBytes());
            return gson.fromJson(json, JsonElement.class);
        } catch (IOException e) {
            throw Sneaky.wrap(e);
        }
    }

    public boolean saveJson(String object) {
        return saveJson(gson.fromJson(object, JsonObject.class));
    }

    public boolean saveJson(JsonObject object) {
        try (final var printWriter = wrap(() -> new PrintWriter(getStuffResourceUrl().getPath())).get()) {
            printWriter.print(gson.toJson(object));
            printWriter.flush();
            return true;
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
