package main.java.filecopier.copier;

import java.io.IOException;
import java.nio.file.*;

public class Nio2FileCopier implements FileCopier {

    @Override
    public void copy(String sourcePath, String destPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path dest = Paths.get(destPath);

        if (source.toAbsolutePath().equals(dest.toAbsolutePath())) {
            throw new IOException("Нельзя скопировать файл сам в себя");
        }

        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
    }
}