package ng.apmis.audreymumplus.ui.pills;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

/**
 * Created by Thadeus-APMIS on 8/20/2018.
 */

@Entity(tableName = "pillreminder")
public class PillModel {

    @PrimaryKey(autoGenerate = true)
    private long _id;
    private String pillName;
    private String qtyPerTime;
    private String frequency;
    private String unit;
    private String duration;
    private String instruction;
    private ArrayList<Long> pillTimes;
    private int muteReminder;

    public PillModel(String pillName, String qtyPerTime, String frequency, String unit, String duration, String instruction, ArrayList<Long> pillTimes, int muteReminder) {
        this.pillName = pillName;
        this.qtyPerTime = qtyPerTime;
        this.frequency = frequency;
        this.unit = unit;
        this.duration = duration;
        this.instruction = instruction;
        this.pillTimes = pillTimes;
        this.muteReminder = muteReminder;
    }

    @Ignore()
    public PillModel(long _id, String pillName, String qtyPerTime, String frequency, String unit, String duration, String instruction, ArrayList<Long> pillTimes, int muteReminder) {
        this._id = _id;
        this.pillName = pillName;
        this.qtyPerTime = qtyPerTime;
        this.frequency = frequency;
        this.unit = unit;
        this.duration = duration;
        this.instruction = instruction;
        this.pillTimes = pillTimes;
        this.muteReminder = muteReminder;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public String getQtyPerTime() {
        return qtyPerTime;
    }

    public void setQtyPerTime(String qtyPerTime) {
        this.qtyPerTime = qtyPerTime;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public ArrayList<Long> getPillTimes() {
        return pillTimes;
    }

    public void setPillTimes(ArrayList<Long> pillTimes) {
        this.pillTimes = pillTimes;
    }

    public int getMuteReminder() {
        return muteReminder;
    }

    public void setMuteReminder(int muteReminder) {
        this.muteReminder = muteReminder;
    }

    @Override
    public String toString() {
        return "PillModel{" +
                "_id=" + _id +
                ", pillName='" + pillName + '\'' +
                ", qtyPerTime='" + qtyPerTime + '\'' +
                ", frequency='" + frequency + '\'' +
                ", unit='" + unit + '\'' +
                ", duration='" + duration + '\'' +
                ", instruction='" + instruction + '\'' +
                ", pillTimes=" + pillTimes +
                ", muteReminder=" + muteReminder +
                '}';
    }
}
