package com.karasu256.mcmidi.config;

import com.karasu256.karasunikilib.config.IConfigFieldsAdapter;
import com.karasu256.karasunikilib.config.IConfigProvider;
import com.karasu256.mcmidi.config.impl.AutoConfigFieldsAdapter;
import com.karasu256.mcmidi.config.impl.AutoConfigProvider;

public class ConfigManager implements IConfigManager {
    private static final AutoConfigProvider<ModConfig> PROVIDER = new AutoConfigProvider<>(ModConfig.class);
    private static final IConfigFieldsAdapter FIELDS_ADAPTER = new AutoConfigFieldsAdapter();
    private static final ConfigManager INSTANCE = new ConfigManager();

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    @Override
    public ModConfig getConfig() {
        return PROVIDER.load();
    }

    @Override
    public IConfigProvider<ModConfig> getProvider() {
        return PROVIDER;
    }

    @Override
    public IConfigFieldsAdapter getFieldsAdapter() {
        return FIELDS_ADAPTER;
    }
}
