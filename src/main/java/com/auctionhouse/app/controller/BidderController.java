package com.auctionhouse.app.controller;

import com.auctionhouse.app.request.BidderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/bidder")
public class BidderController {
    @GetMapping(value = "/getBidList")
    public Map<String, Object> getBidList(){
        Map<String, Object> response  = new HashMap<>();
        ArrayList<String> bidList = new ArrayList<>();
        bidList.add("Porche 911 GT3RS, $398,000");
        bidList.add("Toyota Trueno AE86, $34,000");
        response.put("bidList", bidList);
        return response;
    }
}
