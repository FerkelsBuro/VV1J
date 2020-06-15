package infrastructure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.ConnectionFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;

/**
 * Class that allows to read a ConfigJson
 */
public class JsonConfigReader {
    public static final String DEFAULT_CONFIG_PATH = Paths.get(System.getProperty("user.dir"))
            + File.separator + "config.json";

    private JsonConfigReader() {
    }

    public static void readConfigJson(String configPath, ConnectionFactory factory) throws FileNotFoundException {
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(configPath));
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        factory.setHost(jsonObject.get("MessageBusURL").getAsString());
        factory.setPort(jsonObject.get("Port").getAsInt());
    }
}
