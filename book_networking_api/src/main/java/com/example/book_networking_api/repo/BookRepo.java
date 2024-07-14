package com.example.book_networking_api.repo;

import com.example.book_networking_api.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface BookRepo extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @Query(value = " SELECT book FROM Book book WHERE book.archived = false AND book.shareable = true")
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);

}
