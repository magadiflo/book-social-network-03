package dev.magadiflo.book.network.app.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long> {

    @Query("""
            SELECT history
            FROM BookTransactionHistory AS history
            WHERE history.userId = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, String userId);

    @Query("""
            SELECT history
            FROM BookTransactionHistory AS history
            WHERE history.book.createdBy = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, String userId);

    @Query("""
            SELECT (COUNT(*) > 0) AS isBorrowed
            FROM BookTransactionHistory AS history
            WHERE history.userId = :userId AND
                    history.book.id = :bookId AND
                    history.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Long bookId, String userId);

    @Query("""
            SELECT history
            FROM BookTransactionHistory AS history
            WHERE history.userId = :userId AND
                    history.book.id = :bookId AND
                    history.returned = false AND
                    history.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Long bookId, String userId);

    @Query("""
            SELECT history
            FROM BookTransactionHistory AS history
            WHERE history.book.createdBy = :userId AND
                    history.book.id = :bookId AND
                    history.returned = true AND
                    history.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Long bookId, String userId);
}
