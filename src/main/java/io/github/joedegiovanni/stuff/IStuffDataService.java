package io.github.joedegiovanni.stuff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static io.github.joedegiovanni.stuff.Sneaky.wrap;

public interface IStuffDataService {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    void resetToDefaultStuff();

    JsonElement readJson();

    boolean saveJson(String object);

    boolean saveJson(JsonObject object);

}
