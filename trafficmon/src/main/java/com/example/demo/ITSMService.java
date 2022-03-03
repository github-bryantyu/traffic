package com.example.demo;

import java.util.List;

public interface ITSMService {
	
	List<TSMRow> findAll();
	
	void save(TSMRow row);
	
	void saveAll(List<TSMRow> rows);

}
