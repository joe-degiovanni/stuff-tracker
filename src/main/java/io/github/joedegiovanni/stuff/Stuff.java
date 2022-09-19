package io.github.joedegiovanni.stuff;

import java.util.Set;

public record Stuff(String name, String description, Set<String> labels, Set<Stuff> nestedStuff) {

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
