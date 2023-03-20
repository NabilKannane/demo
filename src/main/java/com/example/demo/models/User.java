package com.example.demo.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "T_users")
@Data
public class User extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;

}
