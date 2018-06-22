package ng.apmis.audreymumplus.ui.Appointments;

/**
 * Created by Thadeus on 6/21/2018.
 */

public class Appointment {

    long ID;
    String title, startTime, dateMonth, dayOfWeek;

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
