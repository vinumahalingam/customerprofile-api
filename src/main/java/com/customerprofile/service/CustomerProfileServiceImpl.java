package com.customerprofile.service;

import com.customerprofile.entity.CustomerProfile;
import com.customerprofile.exception.CustomerProfileNotFoundException;
import com.customerprofile.model.CustomerProfileDTO;
import com.customerprofile.repository.CustomerProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerProfileServiceImpl.class);

    @Autowired
    private CustomerProfileRepository customerProfileRepository;

    @Override
    public List<CustomerProfileDTO> findAll() throws CustomerProfileNotFoundException {
        return customerProfileRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerProfileDTO findByID(String id) throws CustomerProfileNotFoundException {
        try {
            Long profileId = Long.parseLong(id);
            CustomerProfile profile = customerProfileRepository.findById(profileId)
                    .orElseThrow(() -> new CustomerProfileNotFoundException("Customer profile not found with id: " + id));
            return mapToDTO(profile);
        } catch (NumberFormatException e) {
            throw new CustomerProfileNotFoundException("Invalid customer profile ID format: " + id);
        }
    }

    @Override
    public List<CustomerProfileDTO> findByFirstName(String firstName) throws CustomerProfileNotFoundException {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new CustomerProfileNotFoundException("First name cannot be empty");
        }
        List<CustomerProfile> profiles = customerProfileRepository.findByFirstName(firstName);
        if (profiles.isEmpty()) {
            throw new CustomerProfileNotFoundException("No customer profiles found with first name: " + firstName);
        }
        return profiles.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerProfileDTO> findByLastName(String lastName) throws CustomerProfileNotFoundException {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new CustomerProfileNotFoundException("Last name cannot be empty");
        }
        List<CustomerProfile> profiles = customerProfileRepository.findByLastName(lastName);
        if (profiles.isEmpty()) {
            throw new CustomerProfileNotFoundException("No customer profiles found with last name: " + lastName);
        }
        return profiles.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerProfileDTO> findByFullName(String firstName, String lastName) throws CustomerProfileNotFoundException {
        if ((firstName == null || firstName.trim().isEmpty()) || (lastName == null || lastName.trim().isEmpty())) {
            throw new CustomerProfileNotFoundException("First name and last name cannot be empty");
        }
        List<CustomerProfile> profiles = customerProfileRepository.findByFirstNameAndLastName(firstName, lastName);
        if (profiles.isEmpty()) {
            throw new CustomerProfileNotFoundException("No customer profiles found with name: " + firstName + " " + lastName);
        }
        return profiles.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private CustomerProfileDTO mapToDTO(CustomerProfile profile) {
        CustomerProfileDTO dto = new CustomerProfileDTO();
        dto.setId(profile.getId());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setEmail(profile.getEmail());
        dto.setPhone(profile.getPhone());
        dto.setAddressLine1(profile.getAddressLine1());
        dto.setAddressLine2(profile.getAddressLine2());
        dto.setCity(profile.getCity());
        dto.setState(profile.getState());
        dto.setPostalCode(profile.getPostalCode());
        dto.setCountry(profile.getCountry());
        return dto;
    }
}
