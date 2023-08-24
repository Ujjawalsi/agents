package com.example.agents.teUsage;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ms_te_usage")
public class TeUsageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "json_document" ,length = 10000)
    private String jsonDocument;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJsonDocument() {
		return jsonDocument;
	}

	public void setJsonDocument(String jsonDocument) {
		this.jsonDocument = jsonDocument;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void setString(String jsonNode) {
		// TODO Auto-generated method stub
		
	}

	
    
}
