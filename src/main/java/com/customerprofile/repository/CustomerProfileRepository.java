package com.customerprofile.repository;

import com.customerprofile.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
    Optional<CustomerProfile> findByEmail(String email);
    List<CustomerProfile> findByFirstName(String firstName);
    List<CustomerProfile> findByLastName(String lastName);
    List<CustomerProfile> findByFirstNameAndLastName(String firstName, String lastName);
    
    @Query("SELECT COALESCE(MAX(c.id), 0) FROM CustomerProfile c")
    Long findMaxId();
}
