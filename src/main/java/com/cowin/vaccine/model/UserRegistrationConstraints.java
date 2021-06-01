package com.cowin.vaccine.model;

public class UserRegistrationConstraints {

    int age;
    String vaccine;
    String email;

    public UserRegistrationConstraints() {}

    public UserRegistrationConstraints(int age, String vaccine, String email) {
        this.age = age;
        this.vaccine = vaccine;
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
