package org.example.xphrtestingassignment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;
}