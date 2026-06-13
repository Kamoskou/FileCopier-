package main.java.filecopier.history;

import java.io.*;
import java.util.*;

public class HistoryManager {

    private static final String HISTORY_FILE = "data/history.txt";
    private static final int MAX_DISPLAY_COUNT = 10;

    private final List<HistoryEntry> historyList = new ArrayList<>();

    public void load() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                HistoryEntry entry = HistoryEntry.fromString(line);
                if (entry != null) {
                    historyList.add(entry);
                }
            }
            // сортировка от новых к старым
            historyList.sort((a, b) -> Long.compare(b.getLastUsed(), a.getLastUsed()));
        } catch (IOException e) {
            System.err.println("Не удалось загрузить историю: " + e.getMessage());
        }
    }

    public void save() {
        File file = new File(HISTORY_FILE);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (HistoryEntry entry : historyList) {
                writer.write(entry.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Не удалось сохранить историю: " + e.getMessage());
        }
    }

    public void addOrUpdate(String source, String dest, int method) {
        for (HistoryEntry entry : historyList) {
            if (entry.getSource().equals(source) && entry.getDest().equals(dest)) {
                entry.update(method);
                save();
                return;
            }
        }

        historyList.add(0, new HistoryEntry(source, dest, method));
        save();
    }

    public void display() {
        if (historyList.isEmpty()) {
            System.out.println("История копирований пуста.\n");
            return;
        }

        System.out.println("=== ПОСЛЕДНИЕ КОПИРОВАНИЯ ===");
        System.out.println("(Введите номер в квадратных скобках, например [1])\n");

        int limit = Math.min(historyList.size(), MAX_DISPLAY_COUNT);
        for (int i = 0; i < limit; i++) {
            HistoryEntry entry = historyList.get(i);
            String methodName = getMethodName(entry.getMethod());
            System.out.printf("[%d] %s -> %s (%s)%n",
                    i + 1, entry.getSource(), entry.getDest(), methodName);
        }
        System.out.println();
    }

    public String getSourceByIndex(int index) {
        if (index >= 0 && index < historyList.size()) {
            return historyList.get(index).getSource();
        }
        return null;
    }

    public String getDestByIndex(int index) {
        if (index >= 0 && index < historyList.size()) {
            return historyList.get(index).getDest();
        }
        return null;
    }

    private String getMethodName(int method) {
        switch (method) {
            case 1: return "IO";
            case 2: return "NIO";
            case 3: return "NIO.2";
            default: return "?";
        }
    }

    public boolean isEmpty() {
        return historyList.isEmpty();
    }
}