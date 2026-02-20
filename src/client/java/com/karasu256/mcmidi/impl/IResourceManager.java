package com.karasu256.mcmidi.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IResourceManager extends IMidiDataSource {
    String getDirectory();

    String[] getExtensions();

    File getDirectoryFile();

    void ensureDirectoryExists();

    void openDirectory();

    List<String> listLocalFiles();

    Optional<File> resolveFile(String filename);

    String toNormalizedPath(File file);

    File createTempFile(byte[] data) throws IOException;
}
