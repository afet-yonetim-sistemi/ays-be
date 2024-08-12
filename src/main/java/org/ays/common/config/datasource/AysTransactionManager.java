package org.ays.common.config.datasource;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.TransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

@RequiredArgsConstructor
class AysTransactionManager implements PlatformTransactionManager {

    private final PlatformTransactionManager platformTransactionManager;

    @Override
    public @NotNull TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        boolean isReadOnly = definition != null && definition.isReadOnly();
        AysTransactionRoutingDataSource.setReadonlyDataSource(isReadOnly);
        return platformTransactionManager.getTransaction(definition);
    }

    @Override
    public void commit(@NotNull TransactionStatus status) throws TransactionException {
        platformTransactionManager.commit(status);
    }

    @Override
    public void rollback(@NotNull TransactionStatus status) throws TransactionException {
        platformTransactionManager.rollback(status);
    }

}
