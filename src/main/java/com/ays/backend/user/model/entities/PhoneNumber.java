package com.ays.backend.user.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Phone numbers entity which can be assigned to the user object.
 */
@Entity
@Table(name = "phoneNumbers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneNumber extends BaseEntity {

    @Column(nullable = false)
    private Integer countryCode;

    @Column(nullable = false)
    private Integer lineNumber;

    @OneToOne(mappedBy = "phoneNumber", cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
