package com.example.book_networking_api.service;

import com.example.book_networking_api.dto.BookResponseDTO;
import com.example.book_networking_api.dto.BookSaveRequestDTO;
import com.example.book_networking_api.dto.BorrowedBookHistoryResponseDTO;
import com.example.book_networking_api.utill.PageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
    Integer save(BookSaveRequestDTO request, Authentication connectedUser);

    BookResponseDTO findByID(Integer bookId);

    PageResponse<BookResponseDTO> findAllBooks(int page, int size, Authentication connectedUser);

    PageResponse<BookResponseDTO> findAllBooksByOwner(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookHistoryResponseDTO> findAllBorrowedBooks(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookHistoryResponseDTO> findAllReturnedBooks(int page, int size, Authentication connectedUser);

    Integer updateShareableStatus(Integer bookId, Authentication connectedUser);

    Integer updateArchiveStatus(Integer bookId, Authentication connectedUser);

    Integer borrowBook(Integer bookId, Authentication connectedUser);

    Integer returnBorrowedBook(Integer bookId, Authentication connectedUser);

    Integer approveReturnBorrowedBook(Integer bookId, Authentication connectUser);

    void uploadBookCoverPicture(MultipartFile file, Authentication connectUser, Integer bookId);
}
