package io.github.joedegiovanni.stuff;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.before;
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

        staticFiles.externalLocation(new File("src/main/resources/public").getAbsolutePath());

        path("/api", () -> {
            before("/*", (req, res) -> res.header("Content-Type", "application/json"));
            path("/stuff", () -> {
                get("", (req, res) -> service.readJson());
                post("", (req, res) -> service.saveJson(req.body()));
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
