package io.github.joedegiovanni.stuff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static spark.Spark.*;

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

        staticFiles.externalLocation(new File("src/main/resources/public").getAbsolutePath());

        path("/api", () -> {
            before("/*", (req, res) -> res.header("Content-Type", "application/json"));
            path("/stuff", () -> {
                get("", (req, res) -> service.readJson());
                get("/find", (req, res) -> service.readJson());
            });
            path("/users", () -> {
                get("", (req, res) -> "{}");
                post("", (req, res) -> "{}");
                put("", (req, res) -> "{}");
                delete("", (req, res) -> "{}");
            });
        });

    }

}
