package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Arrays;

/**
 * Created by Thadeus on 6/21/2018.
 */

@Entity(tableName = "person")
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
    private String expectedDateOfDelivery;
    private String profileImage;

    @Ignore
    public Person(String _id, String firstName, String lastName, String email, String personId, String dateOfBirth, String motherMaidenName, String primaryContactPhoneNo, String expectedDateOfDelivery, String profileImage) {
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personId = personId;
        this.dateOfBirth = dateOfBirth;
        this.motherMaidenName = motherMaidenName;
        this.primaryContactPhoneNo = primaryContactPhoneNo;
        this.expectedDateOfDelivery = expectedDateOfDelivery;
        this.profileImage = profileImage;
    }

    public Person(int id, String _id, String firstName, String lastName, String email, String personId, String dateOfBirth, String motherMaidenName, String primaryContactPhoneNo, String expectedDateOfDelivery, String profileImage) {
        this.id = id;
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personId = personId;
        this.dateOfBirth = dateOfBirth;
        this.motherMaidenName = motherMaidenName;
        this.primaryContactPhoneNo = primaryContactPhoneNo;
        this.expectedDateOfDelivery = expectedDateOfDelivery;
        this.profileImage = profileImage;
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
        return expectedDateOfDelivery;
    }

    public String getProfileImage () {
        return profileImage;
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
                ", expectedDateOfDelivery='" + expectedDateOfDelivery + '\'' +
                ", profileImage=" + profileImage +
                '}';
    }
}
