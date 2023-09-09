package com.auctionhouse.app.service;

import com.auctionhouse.app.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class AdminService {
    @Autowired//letting framework know to instantiate the class below for us.
    private UserRepository userRepository;
    public Map<String, Object> getUserList(){
        Map<String, Object> response = new HashMap<>();
        response.put("userList", userRepository.findAll());
        return response;
    }
}
