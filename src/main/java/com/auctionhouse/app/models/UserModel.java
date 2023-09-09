package com.auctionhouse.app.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "user")
public class UserModel {
    @Id
    private ObjectId id;
    private String name;
    private Map<String, Object> email;
    private Map<String, Object> address;
    private Map<String, Object> phone;

    private int age;

}
