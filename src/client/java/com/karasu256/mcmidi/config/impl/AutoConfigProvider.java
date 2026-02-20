package com.karasu256.mcmidi.config.impl;

import com.karasu256.karasunikilib.config.IConfigProvider;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class AutoConfigProvider<T extends ConfigData> implements IConfigProvider<T> {
    private final Class<T> configClass;
    private ConfigHolder<T> holder;

    public AutoConfigProvider(Class<T> configClass) {
        this.configClass = configClass;
    }

    @Override
    public void register() {
        if (this.holder == null) {
            this.holder = AutoConfig.register(configClass, GsonConfigSerializer::new);
        }
    }

    @Override
    public void save() {
        if (this.holder != null) {
            this.holder.save();
        }
    }

    @Override
    public void save(T value) {
        if (this.holder != null) {
            this.holder.setConfig(value);
            this.holder.save();
        }
    }

    @Override
    public T load() {
        if (this.holder == null) {
            this.holder = AutoConfig.getConfigHolder(configClass);
        }
        return this.holder.getConfig();
    }

    public ConfigHolder<T> getHolder() {
        if (this.holder == null) {
            this.holder = AutoConfig.getConfigHolder(configClass);
        }
        return this.holder;
    }
}
