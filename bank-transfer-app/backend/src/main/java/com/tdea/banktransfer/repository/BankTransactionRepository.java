package com.tdea.banktransfer.repository;

import com.tdea.banktransfer.entity.BankTransaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Data access for {@link BankTransaction}. */
public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

    /**
     * Lists every transaction, newest first, loading both accounts in the same query.
     *
     * @return all transactions
     */
    @Query("""
            select t from BankTransaction t
            join fetch t.sourceAccount s
            join fetch t.targetAccount d
            order by t.createdAt desc""")
    List<BankTransaction> findAllWithAccounts();

    /**
     * Lists the transactions where the account is the sender or the receiver, newest first.
     *
     * @param accountNumber the account number
     * @return matching transactions
     */
    @Query("""
            select t from BankTransaction t
            join fetch t.sourceAccount s
            join fetch t.targetAccount d
            where s.accountNumber = :accountNumber or d.accountNumber = :accountNumber
            order by t.createdAt desc""")
    List<BankTransaction> findByAccountNumber(@Param("accountNumber") String accountNumber);
}
