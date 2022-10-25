package io.github.joedegiovanni.stuff;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StuffServiceTest {
    IStuffDataService service;

    @BeforeEach
    void setUp() {
        service = new LocalStuffDataService();
        service.resetToDefaultStuff();
    }
    
    @Test
    void record() {
        var stuff = new Stuff("name", "description", null, null);
        assertThat(stuff.name()).isEqualTo("name");
        assertThat(stuff.matchesSearchText("Desc")).isTrue();
        assertThat(stuff.matchesSearchText("waffle")).isFalse();
        
        var stuff2 = Stuff.builder()
            .clone(stuff)
            .withLabels("WAFFLE!", "blah")
            .create();
        assertThat(stuff2.name()).isEqualTo("name");
        assertThat(stuff2.matchesSearchText("Desc")).isTrue();
        assertThat(stuff2.matchesSearchText("waffle")).isTrue();
    }
    
    @Test 
    void loadAndSave() {
        assertThat(service.readJson()).isEqualTo(service.gson.fromJson("""
            {
              "name":"The Farm",
              "description":"My property",
              "labels":[],
              "nestedStuff":[
                {
                  "name":"Log House",
                  "description":"unnamed",
                  "labels":[],
                  "nestedStuff":[
                    {"name":"Kitchen","description":"unnamed","labels":[],"nestedStuff":[]},
                    {"name":"Master Bedroom","description":"unnamed","labels":[],"nestedStuff":[]}
                  ]
                },
                {"name":"Gray Shed","description":"unnamed","labels":[],"nestedStuff":[]},
                {"name":"Red Shed","description":"unnamed","labels":[],"nestedStuff":[]}
              ]
            }
            """, JsonObject.class));
    
        var stuff = Stuff.builder()
            .clone(service.readJson())
            .withDescription("updated")
            .create();
        
        service.saveJson(new JsonObject());
        assertThat(service.readJson()).isEqualTo(service.gson.fromJson("""
            {
              "name":"The Farm",
              "description":"updated",
              "labels":[],
              "nestedStuff":[
                {
                  "name":"Log House",
                  "description":"unnamed",
                  "labels":[],
                  "nestedStuff":[
                    {"name":"Kitchen","description":"unnamed","labels":[],"nestedStuff":[]},
                    {"name":"Master Bedroom","description":"unnamed","labels":[],"nestedStuff":[]}
                  ]
                },
                {"name":"Gray Shed","description":"unnamed","labels":[],"nestedStuff":[]},
                {"name":"Red Shed","description":"unnamed","labels":[],"nestedStuff":[]}
              ]
            }
            """, JsonObject.class));
    }
    

}