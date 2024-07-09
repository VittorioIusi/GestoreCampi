package com.example.gestoreCampi.controller;

import com.example.gestoreCampi.entities.Booking;
import com.example.gestoreCampi.entities.User;
import com.example.gestoreCampi.services.BookingService;
import com.example.gestoreCampi.support.exception.CourtAlreadyBookedException;
import com.example.gestoreCampi.support.exception.NullBuyerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity addBooking(@RequestBody Booking booking) {
        try{
            bookingService.addBooking(booking);
        }
        catch(NullBuyerException nbe){
            return new ResponseEntity("User is null", HttpStatus.BAD_REQUEST);
        }
        catch(CourtAlreadyBookedException cabe){
            return new ResponseEntity("Court is already booked", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Booking added successfully", HttpStatus.OK);
    }//addBooking

    @GetMapping("/byUser")
    public ResponseEntity getBookingByUser(@RequestBody User user,
                                           @RequestParam(defaultValue = "0") int numPage,
                                           @RequestParam(defaultValue = "10") int dimPage,
                                           @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        try {
            List<Booking> ret = bookingService.showBookingByUser(user);
            if(ret.isEmpty())
                return new ResponseEntity("user: "+user.getEmail()+",has no booking",HttpStatus.OK);
            return new ResponseEntity(ret,HttpStatus.OK);
        }
        catch(NullBuyerException nbe){
            return new ResponseEntity("User is null", HttpStatus.BAD_REQUEST);
        }
    }//getBookingByUser



    @GetMapping("/all")
    public ResponseEntity getAll(@RequestParam(defaultValue = "0") int numPage,
                                 @RequestParam(defaultValue = "10") int dimPage,
                                 @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Booking>ret = bookingService.showAll(numPage, dimPage, sortBy);
        if(ret.isEmpty())
            return new ResponseEntity("no booking in db",HttpStatus.OK);
        return new ResponseEntity(ret,HttpStatus.OK);
    }//getAll


    @GetMapping("/byBuyerInPeriod")
    public ResponseEntity getBookingByBuyerInPeriod(@RequestParam Date startDate,
                                                    @RequestParam Date endDate,
                                                    @RequestParam User user,
                                                    @RequestParam(defaultValue = "0") int numPage,
                                                    @RequestParam(defaultValue = "10") int dimPage,
                                                    @RequestParam(value = "sortBy", defaultValue = "id") String sortBy){
        List<Booking> ret = bookingService.showBookingByBuyerInPeriod(startDate,endDate,user,numPage,dimPage,sortBy);
        if(ret.isEmpty())
            return new ResponseEntity("no booking in period",HttpStatus.OK);
        return new ResponseEntity(ret,HttpStatus.OK);
    }//getBookingByBuyerInPeriod


}//BookingController
