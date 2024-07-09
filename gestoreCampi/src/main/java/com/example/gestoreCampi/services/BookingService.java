package com.example.gestoreCampi.services;

import com.example.gestoreCampi.entities.Booking;
import com.example.gestoreCampi.entities.User;
import com.example.gestoreCampi.repositories.BookingRepositoy;
import com.example.gestoreCampi.repositories.CourtRepository;
import com.example.gestoreCampi.support.exception.CourtAlreadyBookedException;
import com.example.gestoreCampi.support.exception.NullBuyerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.example.gestoreCampi.entities.Court;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepositoy bookingRepo;

    @Autowired
    private CourtRepository courtRepo;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public Booking addBooking(Booking booking) throws NullBuyerException, CourtAlreadyBookedException {
        if(booking.getBuyer()==null)
            throw new NullBuyerException();
        Court c = courtRepo.findById(booking.getCourt().getId());
        boolean courtIsBooked = bookingRepo.existsByCourtAndData(booking.getCourt(),booking.getData());//se esisite una entry con la stessa data per lo stesso campo e occupato
        if(courtIsBooked)
            throw new CourtAlreadyBookedException();
        return bookingRepo.save(booking);
    }//addBooking


    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<Booking>showBookingByUser(User buyer) throws NullBuyerException{
        if(buyer == null)
            throw new NullBuyerException();
        return bookingRepo.findByBuyer(buyer);
    }//showBookingByUser


    @Transactional(readOnly = true)
    public List<Booking>showBookingByUser(User user, int numPage, int dimPage, String sortby) throws NullBuyerException {
        if(user == null)
            throw new NullBuyerException();
        Pageable page= PageRequest.of(numPage,dimPage, Sort.by(sortby));
        Page<Booking> pagined = bookingRepo.findByBuyer(user,page);
        if(pagined.hasContent())
            return pagined.getContent();
        return new ArrayList<>();
    }//showBookingByUser


    @Transactional(readOnly = true)
    public List<Booking>showAll(){
        return bookingRepo.findAll();
    }//showAll

    @Transactional(readOnly = true)
    public List<Booking>showAll(int numPage, int dimPage, String sortby){
        Pageable page= PageRequest.of(numPage,dimPage, Sort.by(sortby));
        Page<Booking> paginated = bookingRepo.findAll(page);
        if(paginated.hasContent())
            return paginated.getContent();
        return new ArrayList<>();
    }//showAll


    @Transactional(readOnly = true)
    public List<Booking>showBookingByBuyerInPeriod(Date stratDate, Date endDate, User buyer){
        return bookingRepo.findByBuyerInPeriod(stratDate,endDate,buyer);
    }//showBookingByBuyerInPeriod


    @Transactional(readOnly = true)
    public List<Booking>showBookingByBuyerInPeriod(Date startDate,Date endDate, User buyer, int numPage, int dimPage, String sortby){
        Pageable page= PageRequest.of(numPage,dimPage, Sort.by(sortby));
        Page<Booking> paginated = bookingRepo.findByBuyerInPeriod(startDate,endDate,buyer,page);
        if(paginated.hasContent())
            return paginated.getContent();
        return new ArrayList<>();
    }











}//BookingService
