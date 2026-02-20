package com.karasu256.mcmidi.config;

import com.karasu256.karasunikilib.config.IConfigFieldsAdapter;
import com.karasu256.karasunikilib.config.IConfigProvider;

/**
 * Interface for managing mod configuration.
 */
public interface IConfigManager {
    ModConfig getConfig();

    IConfigProvider<ModConfig> getProvider();

    IConfigFieldsAdapter getFieldsAdapter();
}
