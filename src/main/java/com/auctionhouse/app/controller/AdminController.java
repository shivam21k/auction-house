package com.auctionhouse.app.controller;

import com.auctionhouse.app.request.AdminRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping(value = "/getBidderList")
    public Map<String, Object> getBidderList(){
        Map<String, Object> response  = new HashMap<>();
        ArrayList<String> users = new ArrayList<>();
        users.add("Shivam");
        users.add("Gaurav");
        users.add("Sukanya");
        users.add("Mani");
        response.put("bidderList", users);
        return response;
    }
}
