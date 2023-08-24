package com.example.agents.endpointAgent;


import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ms_endpoint_agents")
public class EndpointAgentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_data", columnDefinition = "text")
    private String agentData;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAgentData() {
		return agentData;
	}

	public void setAgentData(String agentData) {
		this.agentData = agentData;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public EndpointAgentModel(Long id, String agentData, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.agentData = agentData;
		this.createdAt = createdAt;
	}

	public EndpointAgentModel() {
		//super();
	}

    // Getters and setters

    // Constructors

    // Other methods
}
