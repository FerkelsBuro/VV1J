package infrastructure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.ConnectionFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;

public class JsonConfigReader {
    public static final String DEFAULT_CONFIG_PATH = Paths.get(System.getProperty("user.dir")).getParent()
            + File.separator + "Common" + File.separator + "config.json";

    public static void readConfigJson(String configPath, ConnectionFactory factory) throws FileNotFoundException {
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(configPath));
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        factory.setHost(jsonObject.get("MessageBusURL").getAsString());
        factory.setPort(jsonObject.get("Port").getAsInt());
    }
}
