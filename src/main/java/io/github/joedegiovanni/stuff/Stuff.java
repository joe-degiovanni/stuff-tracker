package io.github.joedegiovanni.stuff;

import java.util.Set;

public record Stuff(String name, String description, Set<String> labels, Set<Stuff> nestedStuff) {
    
    public boolean matchesSearchText(String searchText) {
        return toString().toLowerCase().contains(searchText.toLowerCase());
    }
    
    static StuffBuilder builder() {
        return new StuffBuilder();
    }

}
