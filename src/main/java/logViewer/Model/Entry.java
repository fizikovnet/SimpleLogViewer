package logViewer.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by artem on 05.11.2017.
 */
public class Entry {

    public Entry(int id, String time, String level, String clazz, String thread, String message) {
        this.id = new SimpleIntegerProperty(id);
        this.time = new SimpleStringProperty(time);
        this.level = new SimpleStringProperty(level);
        this.clazz = new SimpleStringProperty(clazz);
        this.thread = new SimpleStringProperty(thread);
        this.message = new SimpleStringProperty(message);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        try {
            Date date = dateFormat.parse(time);
            this.datetime = new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private final SimpleIntegerProperty id;
    private final StringProperty time;
    private final StringProperty level;
    private final StringProperty clazz;
    private final StringProperty thread;
    private final StringProperty message;
    private Timestamp datetime = null;
    private String fullMessage = "";

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public String getLevel() {
        return level.get();
    }

    public StringProperty levelProperty() {
        return level;
    }

    public String getClazz() {
        return clazz.get();
    }

    public StringProperty clazzProperty() {
        return clazz;
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }


    public String getThread() {
        return thread.get();
    }

    public StringProperty threadProperty() {
        return thread;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(this.datetime);
    }

    public String getTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        return sdf.format(this.datetime);
    }

    @Override
    public String toString() {
        return time.getValue() + ' ' + level.getValue() + ' ' + thread.getValue() + ' ' + clazz.getValue() + ' ' + message.getValue();
    }
}
