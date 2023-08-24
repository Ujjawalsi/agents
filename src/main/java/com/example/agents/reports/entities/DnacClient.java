package com.example.agents.reports.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dnac_clients_reports_data")
public class DnacClient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "_id")
    private Long id;

    @Column(name = "json_document" , length = 1000000)
    private String jsonDocument;

	@Column(name = "start_Time")
	private Date startTime;

	@Column(name = "end_Time")
	private Date endTime;

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
