package com.auctionhouse.app.dao;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.awt.*;
import java.util.Map;

@Document(collection = "user")
public class User {
    @Id
    private ObjectId id;
    private String name;
    private Map<String, Object> email;
    private Map<String, Object> address;
    private Map<String, Object> phone;

    private int age;

}
