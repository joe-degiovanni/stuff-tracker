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
        BiFunction<String, JsonElement, JsonElement> getOrDefault = (String field, JsonElement orElse) -> clone.getAsJsonObject().has(field) ? clone.getAsJsonObject().get(field) : orElse;
        this.name = getOrDefault.apply("name", new JsonPrimitive("")).getAsString();
        this.description = getOrDefault.apply("description", new JsonPrimitive("")).getAsString();
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

    public StuffBuilder withLabels(String... newLabels) {
        return replaceSet(labels, newLabels);
    }

    public StuffBuilder addLabels(String... labelsToAdd) {
        return addToSet(labels, labelsToAdd);
    }

    public StuffBuilder withNested(Stuff... newNested) {
        return replaceSet(nestedStuff, newNested);
    }

    public StuffBuilder addNested(Stuff... toAdd) {
        return addToSet(nestedStuff, toAdd);
    }

    public <T> StuffBuilder replaceSet(Set<T> set, T... replacements) {
        set.addAll(Arrays.asList(replacements));
        return addToSet(set, replacements);
    }

    public <T> StuffBuilder addToSet(Set<T> set, T... toAdd) {
        set.addAll(Arrays.asList(toAdd));
        return this;
    }

    public Stuff create() {
        return new Stuff(name, description, labels, nestedStuff);
    }

}
