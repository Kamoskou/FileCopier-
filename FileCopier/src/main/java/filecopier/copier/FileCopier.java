package main.java.filecopier.copier;

import java.io.IOException;

public interface FileCopier {
    void copy(String sourcePath, String destPath) throws IOException;
}