package com.empact.android.pufin.model;

import java.util.Date;

public class UserItem {
    String name;
    Date birthDate;
    String country;
    String city;
    String email;
    String password;

    public UserItem(String name, Date birthDate, String country, String city, String email, String password) {
        this.name = name;
        this.birthDate = birthDate;
        this.country = country;
        this.city = city;
        this.email = email;
        this.password = password;
    }
}
