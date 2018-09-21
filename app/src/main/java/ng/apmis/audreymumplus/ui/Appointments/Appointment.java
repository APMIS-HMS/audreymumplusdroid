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

    @PrimaryKey(autoGenerate = true)
    private long _id;
    private String title, appointmentAddress, appointmentDetails;
    private long appointmentTime;
    private int muteAlarm;

    public Appointment(long _id, String title, String appointmentAddress, String appointmentDetails, long appointmentTime, int muteAlarm) {
        this._id = _id;
        this.title = title;
        this.appointmentAddress = appointmentAddress;
        this.appointmentDetails = appointmentDetails;
        this.appointmentTime = appointmentTime;
        this.muteAlarm = muteAlarm;
    }

    @Ignore
    public Appointment(String title, String appointmentAddress, String appointmentDetails, long appointmentTime, int muteAlarm) {
        this.title = title;
        this.appointmentAddress = appointmentAddress;
        this.appointmentDetails = appointmentDetails;
        this.appointmentTime = appointmentTime;
        this.muteAlarm = muteAlarm;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public String getAppointmentAddress() {
        return appointmentAddress;
    }

    public String getAppointmentDetails() {
        return appointmentDetails;
    }

    public long getAppointmentTime() {
        return appointmentTime;
    }

    public int getMuteAlarm() {
        return muteAlarm;
    }

    public void setMuteAlarm(int muteAlarm) {
        this.muteAlarm = muteAlarm;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", appointmentAddress='" + appointmentAddress + '\'' +
                ", appointmentDetails='" + appointmentDetails + '\'' +
                ", appointmentTime=" + appointmentTime +
                ", muteAlarm=" + muteAlarm +
                '}';
    }
}
