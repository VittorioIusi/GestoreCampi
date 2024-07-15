package com.example.gestoreCampi.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data
@Entity
@Table(name="user")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int id;

    @Basic
    @Column(name="user_name",nullable = false,length = 50)
    private String userName;

    @Basic
    @Column(name = "first_name",nullable = true,length = 50)
    private String firstName;

    @Basic
    @Column(name = "last_name",nullable = true,length = 50)
    private String lastName;

    @Basic
    @Column(name = "telephone",nullable = true,length = 20)
    private String telephone;

    @Basic
    @Column(name = "email",nullable = false,length = 90)
    private String email;

    @OneToMany(mappedBy = "buyer",cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<Booking> bookingSet;


    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                '}';
    }
}//User
