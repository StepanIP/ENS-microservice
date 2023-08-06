//package com.example.common.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Table;
//import lombok.*;
//import org.hibernate.annotations.GenericGenerator;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import javax.persistence.*;
//import javax.validation.constraints.Pattern;
//import java.util.Collection;
//import java.util.List;
//
//@Getter @Setter @NoArgsConstructor
//@Entity
//@ToString
//@EqualsAndHashCode
//@Table(name = "users")
//public class User implements UserDetails {
//    @Id
//    @GeneratedValue(generator = "sequence-generator")
//    @GenericGenerator(
//            name = "sequence-generator",
//            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
//            parameters = {
//                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "users_id_seq"),
//                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "10"),
//                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
//            }
//    )
//    private long id;
//
//    @Pattern(regexp = "[A-Z][a-z]+",
//            message = "Must start with a capital letter followed by one or more lowercase letters")
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @Pattern(regexp = "[A-Z][a-z]+",
//            message = "Must start with a capital letter followed by one or more lowercase letters")
//    @Column(name = "surname", nullable = false)
//    private String surname;
//
//    @Pattern(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
//            message = "Invalid email format")
//    private String email;
//
////    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}",
////            message = "Must be minimum 6 characters, at least one letter and one number")
//    private String password;
//
//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private Role role;
//
//    public User(String name, String surname, String email, String password, Role role) {
//        this.name = name;
//        this.surname = surname;
//        this.email = email;
//        this.password = password;
//        this.role = role;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(role);
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled()   {
//        return true;
//    }
//}
