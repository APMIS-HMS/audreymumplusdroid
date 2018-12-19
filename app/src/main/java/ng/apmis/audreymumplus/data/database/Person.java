package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

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
    //Profile image url
    private String profileImage;
    private String profileImageLocalFileName;
    private String week;
    private int day;

    private List<String> forums;

    public Person(){}


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

    public Person(int id, String _id, String firstName, String lastName, String email, String personId, String dateOfBirth, String motherMaidenName, String primaryContactPhoneNo, String expectedDateOfDelivery, String profileImage, String profileImageLocalFileName, String week, int day, List<String> forums) {
        this.id = id;
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personId = personId;
        this.dateOfBirth = dateOfBirth;
        this.motherMaidenName = motherMaidenName;
        this.primaryContactPhoneNo = primaryContactPhoneNo;
        this.ExpectedDateOfDelivery = expectedDateOfDelivery;
        this.profileImage = profileImage;
        this.profileImageLocalFileName = profileImageLocalFileName;
        this.week = week;
        this.day = day;
        this.forums = forums;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getPrimaryContactPhoneNo() {
        return primaryContactPhoneNo;
    }

    public void setPrimaryContactPhoneNo(String primaryContactPhoneNo) {
        this.primaryContactPhoneNo = primaryContactPhoneNo;
    }

    public String getExpectedDateOfDelivery() {
        return ExpectedDateOfDelivery;
    }

    public void setExpectedDateOfDelivery(String expectedDateOfDelivery) {
        ExpectedDateOfDelivery = expectedDateOfDelivery;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageLocalFileName() {
        return profileImageLocalFileName;
    }

    public void setProfileImageLocalFileName(String profileImageLocalFileName) {
        this.profileImageLocalFileName = profileImageLocalFileName;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<String> getForums() {
        return forums;
    }

    public void setForums(List<String> forums) {
        this.forums = forums;
    }

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
                ", profileImageLocalFileName='" + profileImageLocalFileName + '\'' +
                ", week='" + week + '\'' +
                ", day=" + day +
                ", forums=" + forums +
                '}';
    }
}
