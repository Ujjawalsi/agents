package com.example.agents;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ms_te_agents_and_monitors")
public class AgentsAndMonitorsModel {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jsondocument", columnDefinition = "text")
    private String jsonDocument;
    
    

    public AgentsAndMonitorsModel(String jsonDocument) {
		super();
		this.jsonDocument = jsonDocument;
	}

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
}
