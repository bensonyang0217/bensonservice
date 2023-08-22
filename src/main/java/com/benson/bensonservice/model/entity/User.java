package com.benson.bensonservice.model.entity;

import com.benson.bensonservice.constants.AuthProvider;
import com.benson.bensonservice.constants.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String imageUrl;
    @Column(nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;
}
