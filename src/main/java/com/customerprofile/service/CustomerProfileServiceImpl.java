package com.customerprofile.service;

import com.customerprofile.entity.CustomerProfile;
import com.customerprofile.exception.CustomerProfileNotFoundException;
import com.customerprofile.model.CustomerProfileDTO;
import com.customerprofile.model.CustomerProfileRequest;
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

    @Override
    public CustomerProfileDTO createCustomerProfile(CustomerProfileRequest request) throws CustomerProfileNotFoundException {
        if (request == null || request.getFirstName() == null || request.getFirstName().trim().isEmpty() ||
            request.getLastName() == null || request.getLastName().trim().isEmpty() ||
            request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new CustomerProfileNotFoundException("firstName, lastName, and email are required");
        }

        if (customerProfileRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomerProfileNotFoundException("Customer profile already exists with email: " + request.getEmail());
        }

        String accountStatus = request.getAccountStatus() != null ? request.getAccountStatus().trim().toLowerCase() : "active";
        if (!accountStatus.equals("active") && !accountStatus.equals("suspended") && !accountStatus.equals("inactive")) {
            throw new CustomerProfileNotFoundException("accountStatus must be 'active', 'suspended', or 'inactive'");
        }

        try {
            CustomerProfile profile = new CustomerProfile();
            profile.setFirstName(request.getFirstName());
            profile.setLastName(request.getLastName());
            profile.setEmail(request.getEmail());
            profile.setPhone(request.getPhone());
            profile.setAddressLine1(request.getAddressLine1());
            profile.setAddressLine2(request.getAddressLine2());
            profile.setCity(request.getCity());
            profile.setState(request.getState());
            profile.setPostalCode(request.getPostalCode());
            profile.setCountry(request.getCountry() != null ? request.getCountry() : "USA");
            profile.setDateOfBirth(request.getDateOfBirth());
            profile.setGender(request.getGender());
            profile.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
            profile.setAccountStatus(accountStatus);

            CustomerProfile savedProfile = customerProfileRepository.save(profile);
            return mapToDTO(savedProfile);
        } catch (DataIntegrityViolationException e) {
            logger.error("Database constraint violation while creating customer profile", e);
            throw new CustomerProfileNotFoundException("Error creating customer profile: " + e.getRootCause().getMessage());
        }
    }

    @Override
    public CustomerProfileDTO updateCustomerProfile(String id, CustomerProfileRequest request) throws CustomerProfileNotFoundException {
        if (request == null) {
            throw new CustomerProfileNotFoundException("Request body is required");
        }

        try {
            Long profileId = Long.parseLong(id);
            CustomerProfile profile = customerProfileRepository.findById(profileId)
                    .orElseThrow(() -> new CustomerProfileNotFoundException("Customer profile not found with id: " + id));

            // Check if email is being changed and if it already exists
            if (request.getEmail() != null && !request.getEmail().equals(profile.getEmail())) {
                if (customerProfileRepository.findByEmail(request.getEmail()).isPresent()) {
                    throw new CustomerProfileNotFoundException("Customer profile already exists with email: " + request.getEmail());
                }
                profile.setEmail(request.getEmail());
            }

            // Validate account status if provided
            if (request.getAccountStatus() != null) {
                String accountStatus = request.getAccountStatus().trim().toLowerCase();
                if (!accountStatus.equals("active") && !accountStatus.equals("suspended") && !accountStatus.equals("inactive")) {
                    throw new CustomerProfileNotFoundException("accountStatus must be 'active', 'suspended', or 'inactive'");
                }
                profile.setAccountStatus(accountStatus);
            }

            // Update fields if provided (partial update)
            if (request.getFirstName() != null) profile.setFirstName(request.getFirstName());
            if (request.getLastName() != null) profile.setLastName(request.getLastName());
            if (request.getPhone() != null) profile.setPhone(request.getPhone());
            if (request.getAddressLine1() != null) profile.setAddressLine1(request.getAddressLine1());
            if (request.getAddressLine2() != null) profile.setAddressLine2(request.getAddressLine2());
            if (request.getCity() != null) profile.setCity(request.getCity());
            if (request.getState() != null) profile.setState(request.getState());
            if (request.getPostalCode() != null) profile.setPostalCode(request.getPostalCode());
            if (request.getCountry() != null) profile.setCountry(request.getCountry());
            if (request.getDateOfBirth() != null) profile.setDateOfBirth(request.getDateOfBirth());
            if (request.getGender() != null) profile.setGender(request.getGender());
            if (request.getIsActive() != null) profile.setIsActive(request.getIsActive());

            CustomerProfile updatedProfile = customerProfileRepository.save(profile);
            return mapToDTO(updatedProfile);
        } catch (NumberFormatException e) {
            throw new CustomerProfileNotFoundException("Invalid customer profile ID format: " + id);
        } catch (DataIntegrityViolationException e) {
            logger.error("Database constraint violation while updating customer profile", e);
            throw new CustomerProfileNotFoundException("Error updating customer profile: " + e.getRootCause().getMessage());
        }
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
        dto.setDateOfBirth(profile.getDateOfBirth());
        dto.setGender(profile.getGender());
        dto.setIsActive(profile.getIsActive());
        dto.setAccountStatus(profile.getAccountStatus());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        return dto;
    }
}
