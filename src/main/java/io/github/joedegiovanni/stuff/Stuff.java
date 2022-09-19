package io.github.joedegiovanni.stuff;

import java.util.HashSet;
import java.util.Set;

public record Stuff(String name, String description, Set<String> labels, Set<Stuff> nestedStuff) {

    public Stuff(String name, String description, Set<String> labels, Set<Stuff> nestedStuff) {
        this.name = name;
        this.description = description;
        this.labels = labels != null ? labels : new HashSet<>();
        this.nestedStuff = nestedStuff != null ? nestedStuff : new HashSet<>();
    }

    public boolean matchesSearchText(String searchText) {
        return toString().toLowerCase().contains(searchText.toLowerCase());
    }

    public Stuff insert(Stuff... stuffToNest) {
        return builder().clone(this).addNested(stuffToNest).create();
    }

    static StuffBuilder builder() {
        return new StuffBuilder();
    }

}
