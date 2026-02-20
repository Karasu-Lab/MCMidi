package com.karasu256.mcmidi.config;

import com.karasu256.karasunikilib.config.IConfigFieldsAdapter;
import com.karasu256.karasunikilib.config.IConfigProvider;
import com.karasu256.mcmidi.config.impl.AutoConfigFieldsAdapter;
import com.karasu256.mcmidi.config.impl.AutoConfigProvider;

public class ConfigManager {
    private static final AutoConfigProvider<ModConfig> PROVIDER = new AutoConfigProvider<>(ModConfig.class);
    private static final IConfigFieldsAdapter FIELDS_ADAPTER = new AutoConfigFieldsAdapter();

    public static ModConfig getConfig() {
        return PROVIDER.load();
    }

    public static IConfigProvider<ModConfig> getProvider() {
        return PROVIDER;
    }

    public static IConfigFieldsAdapter getFieldsAdapter() {
        return FIELDS_ADAPTER;
    }
}
