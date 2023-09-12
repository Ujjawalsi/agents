package com.example.agents.reports.entities;


import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "ms_thousandeye_alert")
public class ThousandEyeAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "_id")
    private Long id;
    @Column(name = "alert", length = 5000)
    private String alert;

    @Column(name = "event_id")
    private String eventId;
    @Column(name = "event_type")
    private String eventType;
    @Column(name = "new_event_id")
    private String newEventId;

}