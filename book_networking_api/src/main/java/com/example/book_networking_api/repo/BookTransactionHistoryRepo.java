package com.example.book_networking_api.repo;

import com.example.book_networking_api.entity.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@EnableJpaRepositories
public interface BookTransactionHistoryRepo extends JpaRepository<BookTransactionHistory,Integer> {
    @Query(value = "SELECT history FROM BookTransactionHistory history WHERE history.user.id = :userId")
    Page<BookTransactionHistory> findAllBorrowedBooks(Integer userId, Pageable pageable);

    @Query("SELECT history FROM BookTransactionHistory history WHERE history.book.owner.id = :userId")
    Page<BookTransactionHistory> findAllReturnedBooks(Integer userId, Pageable pageable);

    @Transactional
    @Query(value = "SELECT (count(*) > 0) AS isBorrowed FROM book_transaction_history WHERE user_id = :userId AND book_id = :bookId AND return_approved = false", nativeQuery = true)
    boolean isAlreadyBorrowedByUser(@Param("bookId") Integer bookId, @Param("userId") Integer userId);

    @Query(value = "SELECT transaction FROM BookTransactionHistory transaction WHERE transaction.user.id = :userId AND transaction.book.id = :bookId AND transaction.returned = false AND transaction.returnApproved = false")
    Optional <BookTransactionHistory> findBookIdAndUserId (Integer bookId, Integer userId);

    @Query(value = "SELECT transaction FROM BookTransactionHistory transaction WHERE transaction.book.owner.id = :userId AND transaction.book.id = :bookId AND transaction.returned = true AND transaction.returnApproved = false")
    Optional <BookTransactionHistory> findBookIdAndOwnerId(Integer bookId, Integer userId);
}
