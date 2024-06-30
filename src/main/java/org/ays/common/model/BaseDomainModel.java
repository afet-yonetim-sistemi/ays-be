package org.ays.common.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Abstract base class for domain models that includes common auditing fields.
 * <p>
 * The {@code BaseDomainModel} class serves as a foundation for domain entities, providing common fields
 * for tracking creation and update information. It includes metadata such as the user who created the entity,
 * the timestamp of creation, the user who last updated the entity, and the timestamp of the last update.
 * </p>
 *
 * <pre>{@code
 * // Example of a domain model extending BaseDomainModel
 * @Getter
 * @Setter
 * @SuperBuilder
 * public class ExampleDomainModel extends BaseDomainModel {
 *
 *     private String id;
 *     private String name;
 *
 *     // Additional fields and methods specific to the ExampleDomainModel
 * }
 * }</pre>
 *
 * <p>
 * Subclasses of {@code BaseDomainModel} can inherit these fields and are expected to provide
 * entity-specific attributes and methods. This class simplifies the management of audit information
 * across different domain entities, ensuring consistency and reducing duplication.
 * </p>
 *
 * <p>
 * Note that this class is designed to be extended and should not be instantiated directly.
 * </p>
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode
public abstract class BaseDomainModel {

    protected String createdUser;
    protected LocalDateTime createdAt;
    protected String updatedUser;
    protected LocalDateTime updatedAt;

}
