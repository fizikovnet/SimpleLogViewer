package logViewer.Containers;

import java.util.HashSet;

/**
 * Created by artem on 06.11.2017.
 */
public class ThreadContainer {

    private HashSet<String> threads;

    public ThreadContainer() {
        this.threads = new HashSet<>();
        this.threads.add("ALL");
    }

    public HashSet<String> getThreads() {
        return threads;
    }

    public void addThread(String threads) {
        this.threads.add(threads);
    }
}
