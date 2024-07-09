package com.example.gestoreCampi.repositories;

import com.example.gestoreCampi.entities.Booking;
import com.example.gestoreCampi.entities.Court;
import com.example.gestoreCampi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface BookingRepositoy extends JpaRepository<Booking, Integer> {

    Booking findById(int id);
    boolean existsByCourtAndData(Court court, String data); //vedo se il campo è occupato in quell'ora


    List<Booking> findByBuyer(User buyer);
    Page<Booking> findByBuyer(User buyer, Pageable p);

    List<Booking>findAll();
    Page<Booking>findAll(Pageable p);

    //mi prendo le prenotazioni di un utente in un intervallo temporale
    @Query("SELECT b from Booking b WHERE b.purchaseTime > ?1 AND b.purchaseTime < ?2 AND b.buyer = ?3")
    List<Booking> findByBuyerInPeriod(Date startDate,Date endDate, User buyer);
    @Query("SELECT b from Booking b WHERE b.purchaseTime > ?1 AND b.purchaseTime < ?2 AND b.buyer = ?3")
    Page<Booking> findByBuyerInPeriod(Date startDate,Date endDate, User buyer,Pageable p);


}//BookingRepository
