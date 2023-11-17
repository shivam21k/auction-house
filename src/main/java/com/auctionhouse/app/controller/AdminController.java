package com.auctionhouse.app.controller;

import com.auctionhouse.app.models.UserModel;
import com.auctionhouse.app.repository.UserRepository;
import com.auctionhouse.app.request.AdminRequest;
import com.auctionhouse.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @GetMapping(value = "/getUserList")
    public ResponseEntity<Map<String,Object>> getUserList(){
        Map<String,Object> response  = adminService.getUserList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //test comment
}
