package logViewer.Services;

/**
 * Created by artem on 05.11.2017.
 */
public enum RegExpPatterns {

    TIME_PATTERN("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}:\\d{3}\\+\\d{4}"),
    LEVEL_PATTERN("(DEBUG|INFO|WARN|ERROR|TRACE)+"),
    CLASS_PATTERN("\\[[\\S]+\\]"),
    THREAD_PATTERN("<[\\S]+>"),
    MESSAGE_PATTERN("\\]\\s(.*)");

    private final String pattern;

    RegExpPatterns(String s) {
        this.pattern = s;
    }

    public String getPattern() {
        return this.pattern;
    }


}
