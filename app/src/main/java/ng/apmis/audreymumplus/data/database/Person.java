package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.utils.Week;

/**
 * Created by Thadeus on 6/21/2018.
 * Model for each person registered on Audrey
 */

@Entity(tableName = "person", indices = {@Index(value = {"_id"}, unique = true)})
public class Person {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String _id;
    private String firstName;
    private String lastName;
    private String email;

    private String personId;
    private String dateOfBirth;
    private String motherMaidenName;
    private String primaryContactPhoneNo;
    private String ExpectedDateOfDelivery;
    private String profileImage;
    private String week;
    private int day;

    private List<String> forums;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", _id='" + _id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", personId='" + personId + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", motherMaidenName='" + motherMaidenName + '\'' +
                ", primaryContactPhoneNo='" + primaryContactPhoneNo + '\'' +
                ", ExpectedDateOfDelivery='" + ExpectedDateOfDelivery + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", week='" + week + '\'' +
                ", day=" + day +
                ", forums=" + forums +
                '}';
    }

    @Ignore
    public Person(String _id, String firstName, String lastName, String email, String personId, String dateOfBirth, String motherMaidenName, String primaryContactPhoneNo, String ExpectedDateOfDelivery, String profileImage, String week, int day) {
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personId = personId;
        this.dateOfBirth = dateOfBirth;
        this.motherMaidenName = motherMaidenName;
        this.primaryContactPhoneNo = primaryContactPhoneNo;
        this.ExpectedDateOfDelivery = ExpectedDateOfDelivery;
        this.profileImage = profileImage;
        this.week = week;
        this.day = day;
    }

    public Person(int id, String _id, String firstName, String lastName, String email, String personId, String dateOfBirth, String motherMaidenName, String primaryContactPhoneNo, String ExpectedDateOfDelivery, String profileImage, String week, int day, List<String> forums) {
        this.id = id;
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personId = personId;
        this.dateOfBirth = dateOfBirth;
        this.motherMaidenName = motherMaidenName;
        this.primaryContactPhoneNo = primaryContactPhoneNo;
        this.ExpectedDateOfDelivery = ExpectedDateOfDelivery;
        this.profileImage = profileImage;
        this.week = week;
        this.day = day;
        this.forums = forums;
    }

    public int getId() {
        return id;
    }

    public String get_id () {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonId() {
        return personId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public String getPrimaryContactPhoneNo() {
        return primaryContactPhoneNo;
    }

    public String getExpectedDateOfDelivery() {
        return ExpectedDateOfDelivery;
    }

    public String getWeek () {
        return week;
    }

    public int getDay () {
        return day;
    }

    public String getProfileImage () {
        return profileImage;
    }

    public void setWeek (String week) {
        this.week = week;
    }

    public void setDay (int day) {
        this.day = day;
    }

    public List<String> getForums() {
        return forums;
    }

    public void setForums(List<String> forums) {
        this.forums = forums;
    }


}
