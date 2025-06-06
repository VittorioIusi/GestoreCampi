package com.example.gestoreCampi.entities;


import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data
@Entity
@Table(name="booking")

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private int id;

    //data della partita da giocare NON data in cui si è fatta la prenotazione
    @Basic
    @Column(name = "data",nullable = false,length = 15) //slot predifiniti, tipo 1h
    private String data;//gg-MM-aa-HH

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_time")
    private Date purchaseTime;

    @Basic
    @Column(name = "prezzo",nullable = false,length = 15)
    private float prezzo;

    @ManyToOne
    @JoinColumn(name = "buyer")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "court")
    private Court court;
}//Booking
