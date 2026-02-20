package com.karasu256.mcmidi.api;

import com.karasu256.mcmidi.impl.IFileType;
import net.minecraft.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record FileManager<T extends IFileType>(T fileType) {

    public File getDirectoryFile() {
        return new File(fileType.getDirectory());
    }

    public void ensureDirectoryExists() {
        File dir = getDirectoryFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void openDirectory() {
        ensureDirectoryExists();
        Util.getOperatingSystem().open(getDirectoryFile());
    }

    public List<String> listLocalFiles() {
        ensureDirectoryExists();

        List<String> result = new ArrayList<>();
        String[] extensions = fileType.getExtensions();
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

    public Optional<File> resolveFile(String filename) {
        File file = new File(fileType.getDirectory() + "/" + filename);
        if (file.exists()) {
            return Optional.of(file);
        }
        return Optional.empty();
    }

    public String toNormalizedPath(File file) {
        return file.getPath().replace("\\", "/");
    }

    public File createTempFile(byte[] data) throws IOException {
        File tempFile = File.createTempFile(fileType.getDirectory(), fileType.getExtensions()[0]);
        Files.write(tempFile.toPath(), data);
        return tempFile;
    }

    // Extension-agnostic binary loading
    public byte[] loadData(String identifier) throws IOException {
        Optional<File> file = resolveFile(identifier);
        if (file.isPresent()) {
            return Files.readAllBytes(file.get().toPath());
        }
        throw new IOException("File not found: " + identifier);
    }

    public boolean canLoad(String identifier) {
        return resolveFile(identifier).isPresent();
    }
}
