package com.example.common.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;


/**
 * @author Tran Minh Truyen
 */

@Document(collection = "User")
@Data
public class User {

    private int id;

    @Field(value = "Account")
    private String account;

    @Field(value = "Password")
    private String password;

    @Field(value = "FirstName")
    private String firstName;

    @Field(value = "LastName")
    private String lastName;

    @Field(value = "BirthDay")
    private Date birthDay;

    @Field(value = "Address")
    private String address;

    @Field(value = "District")
    private String district;

    @Field(value = "City")
    private String city;

    @Field(value = "PostCode")
    private String postCode;

    @Field(value = "CitizenID")
    private String citizenId;

    @Field(value = "Mail")
    private String email;

    @Field(value = "Role")
    private String role;

    @Field(value = "Image")
    private String image;

    @Field(value = "IsActive")
    private boolean isActive;
}
