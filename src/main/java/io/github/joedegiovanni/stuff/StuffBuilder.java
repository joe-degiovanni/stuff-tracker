package io.github.joedegiovanni.stuff;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class StuffBuilder {

    private String name;
    private String description;
    private Set<String> labels = new HashSet<>();
    private Set<Stuff> nestedStuff = new HashSet<>();
    
    public StuffBuilder clone(Stuff clone) {
        this.name = clone.name();
        this.description = clone.description();
        this.labels = clone.labels();
        this.nestedStuff = clone.nestedStuff();
        return this;
    }
    
    public StuffBuilder clone(JsonElement clone) {
        BiFunction<String, JsonElement, JsonElement> getOrDefault = (String name, JsonElement orElse) -> clone.getAsJsonObject().has(name) ? clone.getAsJsonObject().get(name) : orElse;
        this.name = getOrDefault.apply("name", new JsonPrimitive("unnamed")).getAsString();
        this.description = getOrDefault.apply("description", new JsonPrimitive("unnamed")).getAsString();
        getOrDefault.apply("labels", new JsonArray()).getAsJsonArray().forEach(label -> labels.add(label.getAsString()));
        getOrDefault.apply("nestedStuff", new JsonArray()).getAsJsonArray().forEach(child -> nestedStuff.add(Stuff.builder().clone(child).create()));
        return this;
    }

    public StuffBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public StuffBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public StuffBuilder withLabels(String... labelsToAdd) {
        labels.addAll(Arrays.asList(labelsToAdd));
        return this;
    }

    public StuffBuilder withNested(Stuff... nestedStuff) {
        this.nestedStuff.addAll(Arrays.asList(nestedStuff));
        return this;
    }

    public Stuff create() {
        return new Stuff(name, description, labels, nestedStuff);
    }

}
