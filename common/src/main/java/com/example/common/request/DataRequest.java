package com.example.common.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
public class DataRequest {
    @NotBlank List<String> contacts;
    @NotBlank String message;

    public DataRequest() {
    }

    public DataRequest(@JsonProperty("contacts") String contacts,
                       @JsonProperty("message") String message) {
        this.contacts = extractContacts(contacts);
        this.message = message;
    }

    @Override
    public String toString() {
        return "DataRequest{" +
               "contacts=" + contacts +
               ", message='" + message + '\'' +
               '}';
    }

    private List<String> extractContacts(String contacts){
        String[] contactArray = contacts.split(", ");
        return new ArrayList<>(Arrays.asList(contactArray));
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRequest that = (DataRequest) o;
        return Objects.equals(contacts, that.contacts) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contacts, message);
    }
}