package org.example.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpRequest {

    String firstName;
    String lastName;
    String email;
    String password;



}
