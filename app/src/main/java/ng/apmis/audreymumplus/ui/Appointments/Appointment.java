package ng.apmis.audreymumplus.ui.Appointments;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Thadeus on 6/21/2018.
 */

@Entity(tableName = "appointments")
public class Appointment {

    long ID;
    @PrimaryKey(autoGenerate = true)
    public long _id;
    String title;
    String startTime, dateMonth, dayOfWeek;

    public Appointment(long _id, long ID, String title, String startTime, String dateMonth, String dayOfWeek) {
        this._id = _id;
        this.ID = ID;
        this.title = title;
        this.startTime = startTime;
        this.dateMonth = dateMonth;
        this.dayOfWeek = dayOfWeek;
    }

    @Ignore
    public Appointment(String title, String startTime, String dateMonth, String dayOfWeek) {
        this.title = title;
        this.startTime = startTime;
        this.dateMonth = dateMonth;
        this.dayOfWeek = dayOfWeek;
    }
    @Ignore
    public Appointment(long ID, String title, String startTime, String dateMonth, String dayOfWeek) {
        this.ID = ID;
        this.title = title;
        this.startTime = startTime;
        this.dateMonth = dateMonth;
        this.dayOfWeek = dayOfWeek;
    }

    public long getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDateMonth() {
        return dateMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", startTime='" + startTime + '\'' +
                ", dateMonth='" + dateMonth + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                '}';
    }
}
