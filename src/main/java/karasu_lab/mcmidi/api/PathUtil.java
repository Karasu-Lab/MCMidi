package karasu_lab.mcmidi.api;

import java.nio.file.Path;

public class PathUtil {
    public static Path getResourcePath(Path path, String resourceName, String extension) {
        return path.resolve(resourceName + extension);
    }

    public static boolean isNormal(Path path) {
        return !path.toString().contains("..") && !path.isAbsolute();
    }

    public static boolean isAllowedName(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.matches("[a-z0-9_.-]+");
    }
}
