package com.karasu256.mcmidi.impl;

import java.io.IOException;

public interface IMidiDataSource {
    byte[] loadData(String identifier) throws IOException;

    boolean canLoad(String identifier);
}
