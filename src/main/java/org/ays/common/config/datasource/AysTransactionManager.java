package org.ays.common.config.datasource;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.TransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

/**
 * <p>
 * {@code AysTransactionManager} acts as a wrapper around an existing
 * {@link PlatformTransactionManager} to provide additional transaction
 * management logic.
 * </p>
 *
 * <p>
 * It ensures that the appropriate data source is selected based on the
 * transaction's read-only setting before delegating operations to the
 * underlying transaction manager.
 * </p>
 */
@RequiredArgsConstructor
class AysTransactionManager implements PlatformTransactionManager {

    private final PlatformTransactionManager platformTransactionManager;

    /**
     * <p>
     * Retrieves a transaction status for the provided transaction definition.
     * </p>
     * <p>
     * This method determines whether the transaction is read-only and configures
     * the appropriate data source before delegating the call to the underlying
     * {@link PlatformTransactionManager}.
     * </p>
     * @param definition
     *        the transaction definition, which may include details such as
     *        propagation behavior, isolation level, and read-only status;
     *        may be {@code null}
     * @return the {@link TransactionStatus} representing the current transaction,
     *         allowing operations such as commit or rollback
     * @throws TransactionException
     *         if an error occurs while attempting to start or configure
     *         the transaction
     */
    @Override
    public @NotNull TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        boolean isReadOnly = definition != null && definition.isReadOnly();
        AysTransactionRoutingDataSource.setReadonlyDataSource(isReadOnly);
        return platformTransactionManager.getTransaction(definition);
    }

    /**
     * <p>
     * Commits the transaction associated with the given
     * {@link TransactionStatus}.
     * </p>
     *
     * <p>
     * This method delegates the commit operation to the underlying
     * {@link PlatformTransactionManager}.
     * </p>
     *
     * @param status
     *        the {@link TransactionStatus} representing the current
     *        transaction; must not be {@code null}
     * @throws TransactionException
     *         if an error occurs during the commit process
     */
    @Override
    public void commit(@NotNull TransactionStatus status) throws TransactionException {
        platformTransactionManager.commit(status);
    }

    /**
     * <p>
     * Rolls back the transaction associated with the given
     * {@link TransactionStatus}.
     * </p>
     *
     * <p>
     * This method delegates the rollback operation to the underlying
     * {@link PlatformTransactionManager}.
     * </p>
     *
     * @param status
     *        the {@link TransactionStatus} representing the current
     *        transaction for which the rollback should be performed;
     *        must not be {@code null}
     * @throws TransactionException
     *         if an error occurs during the rollback process
     */
    @Override
    public void rollback(@NotNull TransactionStatus status) throws TransactionException {
        platformTransactionManager.rollback(status);
    }

}
