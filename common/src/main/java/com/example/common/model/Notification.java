package com.example.common.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@EqualsAndHashCode
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "notifications_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "10"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;
    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "name", nullable = false)
    private String name;

    private String message;

    public Notification(String name, String message){
        this.name=name;
        this.message=message;
    }

}
