package org.ays.sos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ays.common.model.entity.BaseEntity;
import org.ays.sos.model.enums.SenderType;

/**
 * A JPA entity class that represents a message in an SOS chat conversation.
 * Messages can be sent by either the user or an operator.
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AYS_SOS_MESSAGE")
public class SosMessageEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "SOS_ID")
    private String sosId;

    @Column(name = "SENDER_TYPE")
    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    @Column(name = "SENDER_ID")
    private String senderId;

    @Column(name = "MESSAGE")
    private String message;

    @jakarta.persistence.Lob
    @Column(name = "IMAGE_URL", columnDefinition = "MEDIUMTEXT")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "SOS_ID", insertable = false, updatable = false)
    private SosEntity sos;

}
