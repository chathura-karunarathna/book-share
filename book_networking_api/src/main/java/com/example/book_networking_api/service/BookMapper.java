package com.example.book_networking_api.service;

import com.example.book_networking_api.dto.BookResponseDTO;
import com.example.book_networking_api.dto.BookSaveRequestDTO;
import com.example.book_networking_api.dto.BorrowedBookHistoryResponseDTO;
import com.example.book_networking_api.entity.Book;
import com.example.book_networking_api.entity.BookTransactionHistory;
import com.example.book_networking_api.utill.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book toBook(BookSaveRequestDTO bookSaveRequestDTO) {
        return Book.builder().id(bookSaveRequestDTO.getId())
                .title(bookSaveRequestDTO.getTitle())
                .authorName(bookSaveRequestDTO.getAuthorName())
                .synopsis(bookSaveRequestDTO.getSynopsis())
                .archived(false)
                .shareable(bookSaveRequestDTO.isShareable())
                .build();
    }

    public BookResponseDTO toBooKResponseDTO(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .owner(book.getOwner().fullName())
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBookHistoryResponseDTO toBorrowedBookHistoryResponseDTO(BookTransactionHistory bookTransactionHistory) {
        return  BorrowedBookHistoryResponseDTO.builder()
                .id(bookTransactionHistory.getBook().getId())
                .title(bookTransactionHistory.getBook().getTitle())
                .authorName(bookTransactionHistory.getBook().getAuthorName())
                .isbn(bookTransactionHistory.getBook().getIsbn())
                .rate(bookTransactionHistory.getBook().getRate())
                .returned(bookTransactionHistory.isReturned())
                .returnApproved(bookTransactionHistory.isReturnApproved())
                .build();
    }
}
