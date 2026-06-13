package main.java.filecopier.ui;

import main.java.filecopier.history.HistoryManager;
import java.io.File;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public void close() {
        scanner.close();
    }

    public String promptSourcePath(HistoryManager history) {
        while (true) {
            System.out.print("Введите путь к ИСХОДНОМУ файлу (или номер [N] из истории, или 'exit'): ");
            String input = scanner.nextLine().trim();

            if (isExitCommand(input)) {
                return null;
            }

            if (input.isEmpty()) {
                System.err.println("Путь не может быть пустым");
                continue;
            }

            String fromHistory = getPathFromHistory(input, history, true);
            if (fromHistory != null) {
                System.out.println("  Используем из истории: " + fromHistory);
                return fromHistory;
            }

            File source = new File(input);
            if (!source.exists()) {
                System.err.println("Файл не существует: " + input);
                continue;
            }

            if (!source.isFile()) {
                System.err.println("Указанный путь ведёт к папке, а не к файлу: " + input);
                continue;
            }

            return input;
        }
    }

    public String promptDestPath(HistoryManager history) {
        while (true) {
            System.out.print("Введите путь к файлу НАЗНАЧЕНИЯ (или номер [N] из истории, или 'exit'): ");
            String input = scanner.nextLine().trim();

            if (isExitCommand(input)) {
                return null;
            }

            if (input.isEmpty()) {
                System.err.println("Путь не может быть пустым");
                continue;
            }

            String fromHistory = getPathFromHistory(input, history, false);
            if (fromHistory != null) {
                System.out.println("  Используем из истории: " + fromHistory);
                return fromHistory;
            }

            return input;
        }
    }

    private String getPathFromHistory(String input, HistoryManager history, boolean isSource) {
        if (history.isEmpty()) {
            return null;
        }

        String numberStr = input;

        if (input.matches("\\[\\d+\\]")) {
            numberStr = input.substring(1, input.length() - 1);
        } else if (!input.matches("\\d+")) {
            return null;
        }

        try {
            int index = Integer.parseInt(numberStr) - 1;
            if (isSource) {
                return history.getSourceByIndex(index);
            } else {
                return history.getDestByIndex(index);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public int promptCopyMethod() {
        while (true) {
            System.out.println("\nВыберите способ копирования:");
            System.out.println("  1 - Классический IO");
            System.out.println("  2 - NIO Channels");
            System.out.println("  3 - NIO.2 Files");
            System.out.print("Ваш выбор (1-3, или 'exit'): ");

            String input = scanner.nextLine().trim();

            if (isExitCommand(input)) {
                return -1;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 3) {
                    return choice;
                }
                System.err.println("Число должно быть 1, 2 или 3");
            } catch (NumberFormatException e) {
                System.err.println("Введите число 1, 2 или 3");
            }
        }
    }

    public boolean askForAnotherCopy() {
        while (true) {
            System.out.print("Скопировать ещё один файл? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("Введите y (да) или n (нет)");
        }
    }

    public boolean confirmOverwrite(String destPath) {
        while (true) {
            System.out.print("Файл '" + destPath + "' уже существует. Перезаписать? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("Введите y (да) или n (нет)");
        }
    }

    public void showCopyResult(long durationMillis) {
        System.out.println("Копирование завершено за " + durationMillis + " мс.");
    }

    public void showError(String message) {
        System.err.println("Ошибка: " + message);
    }

    private boolean isExitCommand(String input) {
        return input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit");
    }
}