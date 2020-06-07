package infrastructure;

import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class JsonConfigReaderTest {
    private static final String configPath = Paths.get(System.getProperty("user.dir")) + File.separator + "test_config.json";

    @Test
    public void readConfigJson() throws FileNotFoundException {
        ConnectionFactory factory = new ConnectionFactory();
        JsonConfigReader.readConfigJson(configPath, factory);

        assertEquals("localhost_test", factory.getHost());
        assertEquals(15672, factory.getPort());
    }
}