package com.example.demo;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TSMService implements ITSMService {

	@Autowired
	private TSMRepository repository;
	
	@Override
	public List<TSMRow> findAll() {
		var tsmRows = (List<TSMRow>) repository.findAll();
		return tsmRows;
	}
	
	@Override
	public void save(TSMRow row) {
		repository.save(row);
	}

	@Override
	public void saveAll(List<TSMRow> rows) {
		repository.saveAll(rows);
	}
}
