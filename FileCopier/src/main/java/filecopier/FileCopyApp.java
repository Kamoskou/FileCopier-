package main.java.filecopier;

import main.java.filecopier.copier.*;
import main.java.filecopier.history.HistoryManager;
import main.java.filecopier.ui.ConsoleUI;
import java.io.IOException;

public class FileCopyApp {

    public static void main(String[] args) {
        System.out.println("=== КОПИРОВАНИЕ ФАЙЛОВ ===\n");

        ConsoleUI ui = new ConsoleUI();
        HistoryManager history = new HistoryManager();

        history.load();
        history.display();

        while (true) {
            String sourcePath = ui.promptSourcePath(history);
            if (sourcePath == null) break;

            String destPath = ui.promptDestPath(history);
            if (destPath == null) break;

            if (new java.io.File(destPath).exists()) {
                if (!ui.confirmOverwrite(destPath)) {
                    System.out.println("Копирование отменено.\n");
                    continue;
                }
            }

            int choice = ui.promptCopyMethod();
            if (choice == -1) break;

            FileCopier copier = createCopier(choice);
            if (copier == null) continue;

            System.out.println("\n--- Копирование ---");
            try {
                long startTime = System.currentTimeMillis();
                copier.copy(sourcePath, destPath);
                long duration = System.currentTimeMillis() - startTime;

                ui.showCopyResult(duration);
                history.addOrUpdate(sourcePath, destPath, choice);

            } catch (IOException e) {
                ui.showError(e.getMessage());
                System.out.println();
                continue;
            }

            if (!ui.askForAnotherCopy()) {
                break;
            }
            System.out.println();
        }

        ui.close();
        System.out.println("Программа завершена.");
    }

    private static FileCopier createCopier(int choice) {
        switch (choice) {
            case 1: return new IoFileCopier();
            case 2: return new NioFileCopier();
            case 3: return new Nio2FileCopier();
            default: return null;
        }
    }
}