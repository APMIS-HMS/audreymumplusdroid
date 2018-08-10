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
    public long _id;
    private String title, appointmentAddress, appointmentDetails;
    private long appointmentTime;

    public Appointment(long _id, String title, String appointmentAddress, String appointmentDetails, long appointmentTime) {
        this._id = _id;
        this.title = title;
        this.appointmentAddress = appointmentAddress;
        this.appointmentDetails = appointmentDetails;
        this.appointmentTime = appointmentTime;
    }

    @Ignore
    public Appointment(String title, String appointmentAddress, String appointmentDetails, long appointmentTime) {
        this.title = title;
        this.appointmentAddress = appointmentAddress;
        this.appointmentDetails = appointmentDetails;
        this.appointmentTime = appointmentTime;
    }

    public long get_id() {
        return _id;
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

    @Override
    public String toString() {
        return "Appointment{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", appointmentAddress='" + appointmentAddress + '\'' +
                ", appointmentDetails='" + appointmentDetails + '\'' +
                ", appointmentTimeEdittext=" + appointmentTime +
                '}';
    }
}
