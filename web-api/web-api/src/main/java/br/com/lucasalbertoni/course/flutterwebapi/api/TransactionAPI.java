package br.com.lucasalbertoni.course.flutterwebapi.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucasalbertoni.course.flutterwebapi.entity.Transaction;

@RestController
@RequestMapping("/transactions")
public class TransactionAPI {
	
	private static final String PASSWORD = "6661"; 
	
	private final Map<String, Transaction> transactions = new HashMap<>();
	
	@GetMapping
	public List<Transaction> get(){
		return new ArrayList<Transaction>(transactions.values());
	}
	
	@PostMapping
	public ResponseEntity<Transaction> set(@RequestHeader String password, @RequestBody Transaction transaction) {
		if(transaction == null || transaction.getValue() == null) {
			return ResponseEntity.badRequest().build();
		}
		if(!PASSWORD.equals(password)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if(transactions.containsKey(transaction.getId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		if(transaction.getId() == null || transaction.getId().isBlank()) {
			transaction.setId(UUID.randomUUID().toString());
		}
		transaction.setDateTime(LocalDateTime.now());
		transactions.put(transaction.getId(), transaction);
		return ResponseEntity.ok(transaction);
	}
	
	@GetMapping("/clean")
	public void clean() {
		for(String key : transactions.keySet()) {
			transactions.remove(key);
		}
	}

}
