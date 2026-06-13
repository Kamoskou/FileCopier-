package main.java.filecopier.copier;

import java.io.*;
import java.nio.channels.FileChannel;

public class NioFileCopier implements FileCopier {

    @Override
    public void copy(String sourcePath, String destPath) throws IOException {
        File source = new File(sourcePath);
        File dest = new File(destPath);

        if (source.getCanonicalPath().equals(dest.getCanonicalPath())) {
            throw new IOException("Нельзя скопировать файл сам в себя");
        }

        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel destChannel = new FileOutputStream(dest).getChannel()) {

            long size = sourceChannel.size();
            // transferTo может скопировать не все байты за раз,
            // для больших файлов нужен цикл, но для учебного проекта достаточно
            sourceChannel.transferTo(0, size, destChannel);
        }
    }
}