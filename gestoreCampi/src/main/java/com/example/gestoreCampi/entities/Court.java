package com.example.gestoreCampi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;

import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data
@Entity
@Table(name = "court", schema = "Rovito")

public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private int id;

    @Basic
    @Column(name = "name",nullable = false,length = 50)
    private String name;

    @Basic
    @Column(name = "type",nullable = false,length = 50)
    private String type;

    @Basic
    @Column(name = "city",nullable = false,length = 50)
    private String city;

    @Basic
    @Column(name = "price_hourly",nullable = false,length = 15)
    private float priceHourly;

    @Basic
    @Column(name = "description", nullable = true, length = 500)
    private String description;

    @OneToMany(mappedBy = "court",cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<Booking>bookings;

    @Version
    @Column(name = "version",nullable = false)
    private long version;//per implementare lock ottimistico, nel campo version viene salvato un numero e
    // dopo che si chiude la transazione si controlla che il numero e lo stesso e si incrementa.
    //Se ci sono due transazioni contomporaneamente il controllo fallisce


}//Court
