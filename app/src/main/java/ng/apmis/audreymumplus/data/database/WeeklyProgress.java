package ng.apmis.audreymumplus.data.database;

import java.util.List;


/**
 * Model for weekly progress items
 * Group Weekly progresses by the week
 */
public class WeeklyProgress {

    private String _id;
    private int week;

    /**
     * A list of all the items in a week
     */
    private List<WeeklyProgressData> data;

    public WeeklyProgress(String _id, int week, List<WeeklyProgressData> data) {
        this._id = _id;
        this.week = week;
        this.data = data;
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

    public void populateWeekData(){
        for (WeeklyProgressData weeklyProgressData : data)
            weeklyProgressData.setWeek(week);
    }
}
