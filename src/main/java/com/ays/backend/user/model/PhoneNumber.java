package com.ays.backend.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phoneNumbers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneNumber extends BaseEntity {

    private Integer countryCode;
    private Integer phoneNumber;
}
