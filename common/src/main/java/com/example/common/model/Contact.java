package com.example.common.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;


@Entity
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

    public Contact() {

    }

    public Contact(String contact, User owner) {
        this.contact = contact;
        this.owner = owner;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public String getContact() {
        return contact;
    }

    public User getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact1 = (Contact) o;
        return id == contact1.id && Objects.equals(contact, contact1.contact) && Objects.equals(owner, contact1.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contact, owner);
    }

    @Override
    public String toString() {
        return "Contact{" +
               "id=" + id +
               ", contact='" + contact + '\'' +
               ", owner=" + owner +
               '}';
    }
}
