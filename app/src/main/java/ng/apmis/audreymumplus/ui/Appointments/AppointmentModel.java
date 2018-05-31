package ng.apmis.audreymumplus.ui.Appointments;

public class AppointmentModel {
    private String event;
    private String event2;
    private String time;
    private String time2;
    private String date;
    private String date2;
    private String day;
    private String day2;
    private String month;


    public AppointmentModel(String mMonth, String mEvent, String mTime, String mDate, String mDay, String mEvent2, String mTime2, String mDate2, String mDay2){
        month = mMonth;
        event = mEvent;
        time = mTime;
        date = mDate;
        day = mDay;
        event2 = mEvent2;
        time2 = mTime2;
        date2 = mDate2;
        day2 = mDay2;
    }

    public String getMonth() {
        return month;
    }

    public String getEvent() {
        return event;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getEvent2() {
        return event2;
    }

    public String getTime2() {
        return time2;
    }

    public String getDate2() {
        return date2;
    }

    public String getDay2() {
        return day2;
    }
}
