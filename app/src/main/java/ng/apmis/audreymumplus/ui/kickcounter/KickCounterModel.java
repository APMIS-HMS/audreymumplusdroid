package ng.apmis.audreymumplus.ui.kickcounter;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


/**
 * Created by Thadeus-APMIS on 8/22/2018.
 */

@Entity(tableName = "kickcounter")
public class KickCounterModel {

    @PrimaryKey(autoGenerate = true)
    private int _id;
    private int kicks;
    private String week;
    private String duration;
    private long date;
    private int day;

    public KickCounterModel(int kicks, String week, String duration, long date, int day) {
        this._id = _id;
        this.kicks = kicks;
        this.week = week;
        this.duration = duration;
        this.date = date;
        this.day = day;
    }

    @Ignore()
    public KickCounterModel(int _id, int kicks, String week, String duration, long date, int day) {
        this._id = _id;
        this.kicks = kicks;
        this.week = week;
        this.duration = duration;
        this.date = date;
        this.day = day;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getKicks() {
        return kicks;
    }

    public void setKicks(int kicks) {
        this.kicks = kicks;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    @Override
    public String toString() {
        return "KickCounterModel{" +
                "_id=" + _id +
                ", kicks=" + kicks +
                ", week='" + week + '\'' +
                ", duration='" + duration + '\'' +
                ", date=" + date +
                ", day=" + day +
                '}';
    }
}
