package com.example.agents.dnacNetworkHealthData;


import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "dnac_network_health_data")
public class DnacNetworkHealthDataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "response", columnDefinition = "text")
    private String response;

	@Column(name = "time_stamp")
    private Date timeStamp;
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

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
