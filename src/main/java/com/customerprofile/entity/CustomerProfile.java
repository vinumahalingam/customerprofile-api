package com.customerprofile.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customerprofile")
@Getter
@Setter
public class CustomerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    private String email;

    private String phone;

    @Column(name="address_line1")
    private String addressLine1;

    @Column(name="address_line2")
    private String addressLine2;

    private String city;

    private String state;

    @Column(name="postal_code")
    private String postalCode;

    private String country;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    private String gender;

    @Column(name="is_active")
    private Boolean isActive;

    @Column(name="account_status")
    private String accountStatus;

    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
