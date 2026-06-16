package io.github.bfur64.terminal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NullMarked;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

@NullMarked
public class Config {
    private static final Logger logger = LogManager.getLogger(Config.class);

    public static String tetrueTerminalVersion = "unknown";
    public static String lanternaVersion = "unknown";
    public static String jlineVersion = "unknown";

    static {
        try (InputStream is = Config.class.getResourceAsStream("/io/github/bfur64/terminal/settings.json")) {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(is);

            tetrueTerminalVersion = jsonNode.get("tetrueTerminalVersion").asString();
            lanternaVersion = jsonNode.get("lanternaVersion").asString();
            jlineVersion = jsonNode.get("jlineVersion").asString();
        }
        catch (IOException e) {
            logger.warn("Could not load version information", e);
        }
    }
}
