package com.example.gestoreCampi.repositories;

import com.example.gestoreCampi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


    List<User> findByFirstName(String firstName);
    Page<User> findByFirstName(String name, Pageable p);

    List<User>findByLastName(String lastName);
    Page<User>findByLastName(String lastName,Pageable p);

    User findByEmail(String Email);
    Page<User>findByTelephone(String telephone,Pageable p);

    List<User>findAll();
    Page<User>findAll(Pageable p);

    User findUserByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsById(int id);
    User findById(int id);
    void deleteById(int id);
    void deleteByEmail(String email);

}//UserRepository
