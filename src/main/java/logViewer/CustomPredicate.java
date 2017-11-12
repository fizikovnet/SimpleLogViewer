package logViewer;

import logViewer.Model.Entry;

import java.util.Map;
import java.util.function.Predicate;

import static logViewer.Controller.MainController.levelStatusFilter;

/**
 * Created by artem on 12.11.2017.
 */
public class CustomPredicate {

    private Predicate<Entry> defaultPredicate = (row) -> true;
    private Predicate<Entry> levelPredicate = null;
    private Predicate<Entry> threadPredicate = (row) -> true;

    public void updateLevelPredicate() {
        levelPredicate = (r) -> {
            for (Map.Entry<String, Boolean> entry : levelStatusFilter.entrySet()){
                if (entry.getKey().equals(r.getLevel()) && entry.getValue()) {
                    return true;
                }
            }
            return false;
        };
    }

    public void updateThreadPredicate(String thread) {
        threadPredicate = (r) -> {
            if (thread.equals("ALL") || r.getThread().equals(thread)) {
                return true;
            }
            return false;
        };
    }

    public void resetLevelPredicate() {
        levelPredicate = (row) -> true;
    }

    private Predicate<Entry> getActualPredicate() {
        return defaultPredicate.and(levelPredicate).and(threadPredicate);
    }

    public boolean applyPredicate(Entry row) {
        return getActualPredicate().test(row);
    }
}
