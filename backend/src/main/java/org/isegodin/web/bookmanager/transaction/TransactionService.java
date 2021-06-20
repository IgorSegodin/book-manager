package org.isegodin.web.bookmanager.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

/**
 * @author isegodin
 */
@Component
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionTemplate transactionTemplate;

    public <T> T readOnlyTransaction(Supplier<T> supplier) {
        return transactionTemplate.execute(transactionStatus -> {
            transactionStatus.setRollbackOnly();

            return supplier.get();
        });
    }

    public <T> T writeTransaction(Supplier<T> supplier) {
        return transactionTemplate.execute(transactionStatus -> {
            return supplier.get();
        });
    }

    public void writeTransaction(Runnable runnable) {
        transactionTemplate.execute(transactionStatus -> {
            runnable.run();
            return null;
        });
    }
}
