package ng.apmis.audreymumplus.data.database;

import java.util.List;

public class WeeklyProgress {

    private String _id;
    private int week;
    private List<WeeklyProgressData> data;

    public WeeklyProgress(String _id, int week, List<WeeklyProgressData> data) {
        this._id = _id;
        this.week = week;
        this.data = data;

        for (WeeklyProgressData weeklyProgressData : data)
            weeklyProgressData.setWeek(week);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public List<WeeklyProgressData> getData() {
        return data;
    }

    public void setData(List<WeeklyProgressData> data) {
        this.data = data;
    }
}
