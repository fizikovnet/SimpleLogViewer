package logViewer.Services;

import javafx.collections.ObservableList;
import logViewer.Model.Entry;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static logViewer.Controller.MainController.counter;

/**
 * Created by artem on 05.11.2017.
 */
public class ParserFileService {

    public void performFile(File file, ObservableList<Entry> container) {
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String s;
            StringBuilder sb = new StringBuilder();
            while((s = br.readLine()) != null) {
                if (!checkIfNewRecord(s)) {
                    sb.append(s + '\n');
                } else if (sb.length() > 0){
                    container.get(counter.get() - 1).setFullMessage(sb.toString());
                    sb.setLength(0);
                } else {
                    container.add(parseLogLine(s));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkIfNewRecord(String s) {
        Pattern r = Pattern.compile(RegExpPatterns.TIME_PATTERN.getPattern(), Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(s);
        return m.find();
    }

    private Entry parseLogLine(String s) {
        String time = null;
        String level = null;
        String clazz = null;
        String thread = null;
        String msg = null;
        Pattern r = Pattern.compile(RegExpPatterns.TIME_PATTERN.getPattern(), Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(s);
        if (m.find()) {
            time = m.group();
        } else {
            return null;
        }
        r = Pattern.compile(RegExpPatterns.LEVEL_PATTERN.getPattern());
        m = r.matcher(s);
        if (m.find()) {
            level = m.group();
        }
        r = Pattern.compile(RegExpPatterns.CLASS_PATTERN.getPattern());
        m = r.matcher(s);
        if (m.find()) {
            clazz = m.group();
        }
        r = Pattern.compile(RegExpPatterns.THREAD_PATTERN.getPattern());
        m = r.matcher(s);
        if (m.find()) {
            thread = m.group();
        }
        r = Pattern.compile(RegExpPatterns.MESSAGE_PATTERN.getPattern());
        m = r.matcher(s);
        if (m.find()) {
            msg = m.group(1);
        }

        return new Entry(counter.getAndIncrement(), time, level, clazz, thread, msg);
    }
}
