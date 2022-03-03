package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TrafficMonController {

    @Autowired
    private ITSMService tsmService;
	
	@GetMapping("/trafficMon/records")
	public ResponseEntity<String> getAllRecords() {
		System.out.println("TrafficMonController Get Request");

		var TSMrecords = (List<TSMRow>) tsmService.findAll();
		System.out.println(TSMrecords); 
		for (int i=0; i< TSMrecords.size(); i++) {
			System.out.println(">> " + TSMrecords.get(i).getLinkId());
		}
		
		return ResponseEntity.ok("You are a good Man!");
	}
	
	@GetMapping("/trafficMon/makeOne")
	public ResponseEntity<String> makeOneRecords() {
		System.out.println("TrafficMonController Get Request");

		TSMRow newRow = new TSMRow();
		newRow.setLinkId("New");
		tsmService.save(newRow);
		
		return ResponseEntity.ok("Made one record!");
	}
	
	@GetMapping("/trafficMon/stopPulling") 
	public ResponseEntity<String> stopPullingTask() {
		System.out.println("TrafficMonController Stop Pulling");

		
		return ResponseEntity.ok("Pulling Task stopped");
	}
}
