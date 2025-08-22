package org.example.xphrtestingassignment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeRecord {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "time_from", nullable = false)
    private LocalDateTime timeFrom;

    @Column(name = "time_to", nullable = false)
    private LocalDateTime timeTo;
}