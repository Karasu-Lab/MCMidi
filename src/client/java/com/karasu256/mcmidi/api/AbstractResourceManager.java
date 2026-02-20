package com.karasu256.mcmidi.api;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.impl.IResourceManager;
import net.minecraft.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractResourceManager implements IResourceManager {

    @Override
    public File getDirectoryFile() {
        return new File(getDirectory());
    }

    @Override
    public void ensureDirectoryExists() {
        File dir = getDirectoryFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void openDirectory() {
        ensureDirectoryExists();
        Util.getOperatingSystem().open(getDirectoryFile());
    }

    @Override
    public List<String> listLocalFiles() {
        ensureDirectoryExists();

        List<String> result = new ArrayList<>();
        String[] extensions = getExtensions();
        File[] files = getDirectoryFile().listFiles((dir, name) -> {
            for (String ext : extensions) {
                if (name.endsWith(ext)) {
                    return true;
                }
            }
            return false;
        });

        if (files != null) {
            for (File file : files) {
                result.add(file.getName());
            }
        }
        return result;
    }

    @Override
    public Optional<File> resolveFile(String filename) {
        File file = new File(getDirectory() + "/" + filename);
        if (file.exists()) {
            return Optional.of(file);
        }
        return Optional.empty();
    }

    @Override
    public String toNormalizedPath(File file) {
        return file.getPath().replace("\\", "/");
    }

    @Override
    public File createTempFile(byte[] data) throws IOException {
        File tempFile = File.createTempFile(getDirectory(), getExtensions()[0]);
        Files.write(tempFile.toPath(), data);
        return tempFile;
    }

    @Override
    public byte[] loadData(String identifier) throws IOException {
        Optional<File> file = resolveFile(identifier);
        if (file.isPresent()) {
            return Files.readAllBytes(file.get().toPath());
        }
        throw new IOException("File not found: " + identifier);
    }

    @Override
    public boolean canLoad(String identifier) {
        return resolveFile(identifier).isPresent();
    }
}
