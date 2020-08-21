package com.spring.reactive.registration.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Document
public class Customer {
    @Id
    private String userId;
    private String password;
    @Size(min = 2,message = "name should have minimum 4 Character")
    private String userName;
    private String firstName;
    private String lastName;
    private String address;
    private String state;
    private String country;
    @Email(message = "Wrong Formate")
    private String email;
    @NotNull(message = "Pan Number  should be not null")
    private String panNumber;
    @NotNull(message = "Contact Number should be not null")
    private Long contactNumber;
    private String DOB;
    @NotNull(message = "Account Type should be not null")
    private String accountType;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public Long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Customer(String userId, String password, String userName, String firstName, String lastName, String address, String state, String country, String email, String panNumber, Long contactNumber, String DOB, String accountType) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.state = state;
        this.country = country;
        this.email = email;
        this.panNumber = panNumber;
        this.contactNumber = contactNumber;
        this.DOB = DOB;
        this.accountType = accountType;
    }
}