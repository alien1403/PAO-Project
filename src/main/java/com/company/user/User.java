package com.company.user;

public class User
{
    public long uniqueId;
    private String typeOfUser;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    public User(){
        this.uniqueId = 0;
        this.typeOfUser = "";
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.password = "";
    }

    public User(long uniqueId, String typeOfUser, String firstName, String lastName, String email, String password) {
        this.uniqueId = uniqueId;
        this.typeOfUser = typeOfUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public long getUniqueId() {
        return uniqueId;
    }
    public void incrementUniqueId(){
        this.uniqueId += 1;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(String typeOfUser) {
        this.typeOfUser = typeOfUser;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return this.firstName + ' ' + this.lastName;
    }
}
