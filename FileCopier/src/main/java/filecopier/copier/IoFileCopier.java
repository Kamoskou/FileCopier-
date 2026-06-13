package main.java.filecopier.copier;

import java.io.*;

public class IoFileCopier implements FileCopier {

    private static final int BUFFER_SIZE = 8192;

    @Override
    public void copy(String sourcePath, String destPath) throws IOException {
        File source = new File(sourcePath);
        File dest = new File(destPath);

        if (source.getCanonicalPath().equals(dest.getCanonicalPath())) {
            throw new IOException("Нельзя скопировать файл сам в себя");
        }

        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}