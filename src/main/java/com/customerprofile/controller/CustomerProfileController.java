package com.customerprofile.controller;

import com.customerprofile.exception.CustomerProfileNotFoundException;
import com.customerprofile.model.CustomerProfileDTO;
import com.customerprofile.service.CustomerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-profiles")
public class CustomerProfileController {

    @Autowired
    private CustomerProfileService customerProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerProfileDTO> getCustomerProfileById(@PathVariable String id) throws CustomerProfileNotFoundException {
        CustomerProfileDTO profile = customerProfileService.findByID(id);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CustomerProfileDTO>> getAllCustomerProfiles() throws CustomerProfileNotFoundException {
        List<CustomerProfileDTO> profiles = customerProfileService.findAll();
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @GetMapping("/search/firstName/{firstName}")
    public ResponseEntity<List<CustomerProfileDTO>> getCustomerProfilesByFirstName(@PathVariable String firstName) throws CustomerProfileNotFoundException {
        List<CustomerProfileDTO> profiles = customerProfileService.findByFirstName(firstName);
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @GetMapping("/search/lastName/{lastName}")
    public ResponseEntity<List<CustomerProfileDTO>> getCustomerProfilesByLastName(@PathVariable String lastName) throws CustomerProfileNotFoundException {
        List<CustomerProfileDTO> profiles = customerProfileService.findByLastName(lastName);
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @GetMapping("/search/fullName")
    public ResponseEntity<List<CustomerProfileDTO>> getCustomerProfilesByFullName(@RequestParam String firstName, @RequestParam String lastName) throws CustomerProfileNotFoundException {
        List<CustomerProfileDTO> profiles = customerProfileService.findByFullName(firstName, lastName);
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }
}
