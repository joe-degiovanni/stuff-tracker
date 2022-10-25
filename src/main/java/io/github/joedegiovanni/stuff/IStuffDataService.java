package io.github.joedegiovanni.stuff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import static io.github.joedegiovanni.stuff.Sneaky.unchecked;

public interface IStuffDataService {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    void resetToDefaultStuff();

    JsonElement readJson();

    boolean saveJson(String object);

    boolean saveJson(JsonObject object);

}
