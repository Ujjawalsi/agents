package com.example.agents;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "ms_te_usage")
public class TeUsageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "json_document", columnDefinition = "jsonb")
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
