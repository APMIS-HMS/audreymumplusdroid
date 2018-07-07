package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Thadeus on 6/21/2018.
 */

@Entity(tableName = "person")
public class Person {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    private String personId;
    private String dateOfBirth;
    private String motherMaidenName;
    private String primaryContactPhoneNo;
    private String expectedDateOfDelivery;

  /*  @Ignore
    public Person(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }*/

    @Ignore
    public Person(String firstName, String lastName, String email, String personId, String dateOfBirth, String motherMaidenName, String primaryContactPhoneNo, String expectedDateOfDelivery) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personId = personId;
        this.dateOfBirth = dateOfBirth;
        this.motherMaidenName = motherMaidenName;
        this.primaryContactPhoneNo = primaryContactPhoneNo;
        this.expectedDateOfDelivery = expectedDateOfDelivery;
    }

   /* public Person(int id, String firstName, String lastName, String email){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }*/

    public Person(int id, String firstName, String lastName, String email, String personId, String dateOfBirth, String motherMaidenName, String primaryContactPhoneNo, String expectedDateOfDelivery) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personId = personId;
        this.dateOfBirth = dateOfBirth;
        this.motherMaidenName = motherMaidenName;
        this.primaryContactPhoneNo = primaryContactPhoneNo;
        this.expectedDateOfDelivery = expectedDateOfDelivery;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", personId='" + personId + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", motherMaidenName='" + motherMaidenName + '\'' +
                ", primaryContactPhoneNo='" + primaryContactPhoneNo + '\'' +
                ", expectedDateOfDelivery='" + expectedDateOfDelivery + '\'' +
                '}';
    }
}
