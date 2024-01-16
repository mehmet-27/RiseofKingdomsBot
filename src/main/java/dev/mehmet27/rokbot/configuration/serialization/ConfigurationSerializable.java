package dev.mehmet27.rokbot.configuration.serialization;

import java.util.Map;

public interface ConfigurationSerializable {
    public Map<String, Object> serialize();
}
