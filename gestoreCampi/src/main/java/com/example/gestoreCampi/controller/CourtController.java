package com.example.gestoreCampi.controller;


import com.example.gestoreCampi.entities.Court;
import com.example.gestoreCampi.services.CourtService;
import com.example.gestoreCampi.support.exception.CourtAlreadyExistException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/courts")
public class CourtController {
    @Autowired
    private CourtService courtService;

    @PostMapping("/add")
    public ResponseEntity addCourt(@RequestBody @Valid Court court) {
        try{
            courtService.addCourt(court);
        }
        catch(CourtAlreadyExistException e){
            return new ResponseEntity("Court already exisit", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Court"+court.getName()+"added!", HttpStatus.OK);
    }//addCourt


    @GetMapping("/byType")
    public ResponseEntity getCourtType(@RequestParam(defaultValue = "tennis") String type,
                                       @RequestParam(defaultValue = "0") int numPage,
                                       @RequestParam(defaultValue = "10") int dimPage,
                                       @RequestParam(value = "sortBy",defaultValue = "name") String sortBy){
        List<Court> ret = courtService.showCourtByType(type,numPage,dimPage,sortBy);
        if(ret.isEmpty())
            return new ResponseEntity("No court found"+type,HttpStatus.OK);
        return new ResponseEntity(ret, HttpStatus.OK);
    }//getCourtType


    @GetMapping("/byCity")
    public ResponseEntity getCourtCity(@RequestParam(defaultValue = "rovito")String city,
                                       @RequestParam(defaultValue = "0")int numPage,
                                       @RequestParam(defaultValue = "10")int dimPage,
                                       @RequestParam(value = "sortBy",defaultValue = "name")String sortBy){
        List<Court> ret = courtService.showCourtByCity(city,numPage,dimPage,sortBy);
        if(ret.isEmpty())
            return new ResponseEntity("No court found in "+city,HttpStatus.OK);
        return new ResponseEntity(ret, HttpStatus.OK);
    }//getCourtCity


    @GetMapping("/byPriceType")
    public ResponseEntity getCourtPriceType(@RequestParam(defaultValue = "10") float price,
                                            @RequestParam(defaultValue = "tennis") String type,
                                            @RequestParam(defaultValue = "0") int numPage,
                                            @RequestParam(defaultValue = "10") int dimPage,
                                            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<Court> ret = courtService.showByPriceAndType(price,type,numPage,dimPage,sortBy);
        if(ret.isEmpty())
            return new ResponseEntity("No court with price: "+price+ ", and type: "+type,HttpStatus.OK);
        return new ResponseEntity(ret,HttpStatus.OK);
    }

    @GetMapping("/byCityType")
    public ResponseEntity getCourtCityType(@RequestParam(defaultValue = "rovito") String city,
                                           @RequestParam(defaultValue = "tennis") String type,
                                           @RequestParam(defaultValue = "0") int numPage,
                                           @RequestParam(defaultValue = "10") int dimPage,
                                           @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<Court> ret = courtService.showByCityAndType(city,type,numPage,dimPage,sortBy);
        if(ret.isEmpty())
            return new ResponseEntity("No court in: "+city+ ", and type: "+type,HttpStatus.OK);
        return new ResponseEntity(ret,HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity getAll(@RequestParam(defaultValue = "0") int numPage,
                                 @RequestParam(defaultValue = "10") int dimPage,
                                 @RequestParam(value = "sortBy", defaultValue = "name") String sortBy){
        List<Court> ret = courtService.showAllCourts(numPage,dimPage,sortBy);
        if(ret.isEmpty())
            return new ResponseEntity("No court registered in the system",HttpStatus.OK);
        return new ResponseEntity(ret,HttpStatus.OK);
    }
}//CourtController
