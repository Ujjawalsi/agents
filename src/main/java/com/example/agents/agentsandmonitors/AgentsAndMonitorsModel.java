package com.example.agents.agentsandmonitors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "ms_te_agents_and_monitors")
public class AgentsAndMonitorsModel {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jsondocument", columnDefinition = "text")
    private String jsonDocument;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;
    
    

    public AgentsAndMonitorsModel() {
		
	}

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
