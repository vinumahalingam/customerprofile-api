package com.customerprofile.service;

import com.customerprofile.exception.CustomerProfileNotFoundException;
import com.customerprofile.model.CustomerProfileDTO;

import java.util.List;

public interface CustomerProfileService {
    List<CustomerProfileDTO> findAll() throws CustomerProfileNotFoundException;
    CustomerProfileDTO findByID(String id) throws CustomerProfileNotFoundException;
    List<CustomerProfileDTO> findByFirstName(String firstName) throws CustomerProfileNotFoundException;
    List<CustomerProfileDTO> findByLastName(String lastName) throws CustomerProfileNotFoundException;
    List<CustomerProfileDTO> findByFullName(String firstName, String lastName) throws CustomerProfileNotFoundException;
}
