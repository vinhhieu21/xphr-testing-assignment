package org.example.xphrtestingassignment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    private Long id;

    @Column(nullable = false, length = 60)
    private String name;
}
