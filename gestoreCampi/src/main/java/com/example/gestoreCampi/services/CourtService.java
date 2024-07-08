package com.example.gestoreCampi.services;


import com.example.gestoreCampi.entities.Court;
import com.example.gestoreCampi.repositories.CourtRepository;
import com.example.gestoreCampi.support.exception.CourtAlreadyExistException;
import com.example.gestoreCampi.support.exception.CourtNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourtService {
    @Autowired
    CourtRepository courtRepo;



    @Transactional(readOnly = false)
    public void addCourt(Court court) throws CourtAlreadyExistException {
        if(courtRepo.existsById(court.getId()) || courtRepo.existsByName(court.getName()))
            throw new CourtAlreadyExistException();
        courtRepo.save(court);
    }//addCourt


    @Transactional(readOnly = false)
    public void deleteCourt(Court court) throws CourtNotExistException {
        if(!courtRepo.existsById(court.getId()))
            throw new CourtNotExistException();
        courtRepo.delete(court);
    }//deleteCourt



    @Transactional(readOnly = true)
    public List<Court>showByPriceAndType(float price,String type){
        return courtRepo.findByPriceHourlyAndType(price,type);
    }//showByPriceAndType


    @Transactional(readOnly = true)
    public List<Court>showByPriceAndType(float price,String type,int numPage,int dimPage,String sortby){
        Pageable page= PageRequest.of(numPage,dimPage, Sort.by(sortby));
        Page<Court> pagined = courtRepo.findByPriceHourlyAndType(price,type,page);
        if(pagined.hasContent())
            return pagined.getContent();
        return new ArrayList<>(); //restituisco una lista vuota cosi non ho problemi con risultati di tipo null
    }//showByPriceAndType


    @Transactional(readOnly = true)
    public List<Court>showByCityAndType(String city,String type){
        return courtRepo.findByCityAndType(city,type);
    }//showByCiryAndType


    @Transactional(readOnly = true)
    public List<Court>showByCityAndType(String city,String type,int numPage,int dimPage,String sortby){
        Pageable page= PageRequest.of(numPage,dimPage, Sort.by(sortby));
        Page<Court>pagined = courtRepo.findByCityAndType(city,type,page);
        if(pagined.hasContent())
            return pagined.getContent();
        return new ArrayList<>(); //restituisco una lista vuota cosi non ho problemi con risultati di tipo null
    }//showByCiryAndType


    @Transactional(readOnly = true)
    public List<Court>showCourtByType(String type){
        return courtRepo.findByType(type);
    }//showCourtByType


    @Transactional
    public List<Court>showCourtByType(String type,int numPage,int dimPage,String sortby){
        Pageable page= PageRequest.of(numPage,dimPage, Sort.by(sortby));
        Page<Court>pagined = courtRepo.findByType(type,page);
        if(pagined.hasContent())
            return pagined.getContent();
        return new ArrayList<>();
    }//showCourtByType


    @Transactional(readOnly = true)
    public List<Court>showCourtByCity(String city){
        return courtRepo.findByCity(city);
    }//showCpourtByCity


    @Transactional
    public List<Court>showCourtByCity(String city,int numPage,int dimPage,String sortby){
        Pageable page= PageRequest.of(numPage,dimPage, Sort.by(sortby));
        Page<Court>pagined = courtRepo.findByCity(city,page);
        if(pagined.hasContent())
            return pagined.getContent();
        return new ArrayList<>();
    }//showCpourtByCity


    @Transactional(readOnly = true)
    public List<Court>showAllCourts(){
        return courtRepo.findAll();
    }//showAllCpurt


    @Transactional
    public List<Court>showAllCourts(int numPage,int dimPage,String sortby){
        Pageable page= PageRequest.of(numPage,dimPage, Sort.by(sortby));
        Page<Court>pagined = courtRepo.findAll(page);
        if(pagined.hasContent())
            return pagined.getContent();
        return new ArrayList<>();
    }//showAllCpurt


    @Transactional(readOnly = true)
    public Court showById(int id){
        return courtRepo.findById(id);
    }//showById

}//CourtServices
