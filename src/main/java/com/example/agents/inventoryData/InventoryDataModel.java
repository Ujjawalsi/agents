package com.example.agents.inventoryData;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ms_inventory")
public class InventoryDataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data", length = 10000)
    private String data;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public InventoryDataModel(Long id, String data, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.data = data;
		this.createdAt = createdAt;
	}
    
	public InventoryDataModel()
	{
		
	}
}
