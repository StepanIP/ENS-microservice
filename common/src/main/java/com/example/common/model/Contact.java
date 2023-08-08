package com.example.common.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;



@Getter @Setter @NoArgsConstructor
@Entity
@ToString
@EqualsAndHashCode
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "users_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "10"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    private String contact;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public Contact(String contact, User owner) {
        this.contact = contact;
        this.owner = owner;
    }
}
