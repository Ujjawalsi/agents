package com.example.agents.dnacNetworkHealthData;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dnac_network_health_data")
public class DnacNetworkHealthDataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "response", columnDefinition = "text")
    private String response;

    private LocalDateTime timeStamp;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	

    // Constructors, getters, setters
}
