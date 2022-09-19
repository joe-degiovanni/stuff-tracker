package io.github.joedegiovanni.stuff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;

public class Server {

    private final Logger log = LoggerFactory.getLogger(Server.class);
    private final StuffDataService service = new StuffDataService();

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        log.info("http://localhost:4567/");
        log.info("http://localhost:4567/api/stuff");
        log.info("http://localhost:4567/api/users");

        staticFiles.location("/public");

        path("/api", () -> {
            path("/stuff", () -> {
                get("", (req, res) -> service.readJson());
                get("/find", (req, res) -> service.readJson());
            });
            path("/users", () -> {
                get("", ((request, response) -> "todo"));
                post("", ((request, response) -> "todo"));
                put("", ((request, response) -> "todo"));
                delete("", ((request, response) -> "todo"));
            });
        });

    }

}
