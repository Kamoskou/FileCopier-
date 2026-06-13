package main.java.filecopier.history;

public class HistoryEntry {
    private final String source;
    private final String dest;
    private int method;
    private long lastUsed;
    private int useCount;

    public HistoryEntry(String source, String dest, int method) {
        this.source = source;
        this.dest = dest;
        this.method = method;
        this.lastUsed = System.currentTimeMillis();
        this.useCount = 1;
    }

    public String getSource() { return source; }
    public String getDest() { return dest; }
    public int getMethod() { return method; }
    public long getLastUsed() { return lastUsed; }
    public int getUseCount() { return useCount; }

    public void update(int method) {
        this.method = method;
        this.lastUsed = System.currentTimeMillis();
        this.useCount++;
    }

    @Override
    public String toString() {
        return source + "|" + dest + "|" + method + "|" + lastUsed + "|" + useCount;
    }

    public static HistoryEntry fromString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 5) {
            try {
                HistoryEntry entry = new HistoryEntry(parts[0], parts[1], Integer.parseInt(parts[2]));
                java.lang.reflect.Field lastUsedField = HistoryEntry.class.getDeclaredField("lastUsed");
                java.lang.reflect.Field useCountField = HistoryEntry.class.getDeclaredField("useCount");
                lastUsedField.setAccessible(true);
                useCountField.setAccessible(true);
                lastUsedField.setLong(entry, Long.parseLong(parts[3]));
                useCountField.setInt(entry, Integer.parseInt(parts[4]));
                return entry;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}