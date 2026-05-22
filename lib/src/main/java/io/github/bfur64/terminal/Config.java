package io.github.bfur64.terminal;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class Config {
    public static String tetrueTerminalVersion;
    public static String lanternaVersion;
    public static String jlineVersion;

    static {
        try (InputStream is = Config.class.getResourceAsStream("/io/github/bfur64/terminal/settings.json")) {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(is);

            tetrueTerminalVersion = jsonNode.get("tetrueTerminalVersion").asString();
            lanternaVersion = jsonNode.get("lanternaVersion").asString();
            jlineVersion = jsonNode.get("jlineVersion").asString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
