package org.example.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class SignUpRequest {
    String firstName;
    String lastName;
    String username;
    String password;
}
