package com.karasu256.mcmidi.config.impl;

import com.karasu256.karasunikilib.config.AbstractReflectedFieldGetter;
import com.karasu256.mcmidi.config.ModConfig;

public class AutoConfigFieldsAdapter extends AbstractReflectedFieldGetter {
    public AutoConfigFieldsAdapter() {
        super(ModConfig.class);
    }
}
