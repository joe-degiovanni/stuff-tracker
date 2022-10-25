package io.github.joedegiovanni.stuff;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static io.github.joedegiovanni.stuff.Sneaky.unchecked;
import static java.nio.charset.StandardCharsets.UTF_8;

public class GoogleCloudStorageStuffDataService implements IStuffDataService {

    public static final String DEFAULT_STUFF_FILE_NAME = "data/default-stuff.json";
    public static final String STUFF_FILE_NAME = "data/my-stuff.json";
    public static final String BUCKET = "stuff-tracker-data";

    public final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public final Storage storage;

    public GoogleCloudStorageStuffDataService() {
        try {
            String cred = System.getenv("STUFF_TRACKER_CREDENTIAL");
            InputStream credStream = cred != null ?
                    new ByteArrayInputStream(cred.getBytes(UTF_8)) :
                    new FileInputStream(System.getenv("STUFF_TRACKER_CREDENTIAL_FILE"));
            storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(credStream))
                .build()
                .getService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resetToDefaultStuff() {
        unchecked(() -> Files.copy(Path.of(DEFAULT_STUFF_FILE_NAME), Path.of(STUFF_FILE_NAME), StandardCopyOption.REPLACE_EXISTING));
    }

    public JsonElement readJson() {
        String json = new String(storage.readAllBytes(BlobId.of(BUCKET, STUFF_FILE_NAME)), UTF_8);
        return gson.fromJson(json, JsonElement.class);
    }

    public boolean saveJson(String object) {
        return saveJson(gson.fromJson(object, JsonObject.class));
    }

    public boolean saveJson(JsonObject object) {
        try (WritableByteChannel channel = storage.get(BlobId.of(BUCKET, STUFF_FILE_NAME)).writer();) {
            channel.write(ByteBuffer.wrap(gson.toJson(object).getBytes(UTF_8)));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
