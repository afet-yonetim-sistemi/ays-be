package com.ays.backend.user.model.entities;

import com.ays.backend.user.model.entities.BaseEntity;
import com.ays.backend.user.model.entities.User;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private Integer countryCode;

    @Column(nullable = false)
    private Integer lineNumber;

    @OneToOne(mappedBy="phoneNumber", cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
