package com.example.book_networking_api.controller;

import com.example.book_networking_api.dto.BookResponseDTO;
import com.example.book_networking_api.dto.BookSaveRequestDTO;
import com.example.book_networking_api.dto.BorrowedBookHistoryResponseDTO;
import com.example.book_networking_api.service.BookService;
import com.example.book_networking_api.utill.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name="Book")
public class BookController {

    private final BookService bookService;

    @PostMapping("/save")
    public ResponseEntity<Integer> saveBook(@Valid @RequestBody BookSaveRequestDTO bookSaveRequestDTO,
                                            Authentication connectedUser){
        return ResponseEntity.ok(bookService.save(bookSaveRequestDTO, connectedUser));
    }

    @GetMapping("{book-id}")
    public ResponseEntity<BookResponseDTO> findBookById(
            @PathVariable("book-id") Integer bookId){
        return ResponseEntity.ok(bookService.findByID(bookId));
    }

    @GetMapping()
    public ResponseEntity<PageResponse<BookResponseDTO>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser ){

        return ResponseEntity.ok(bookService.findAllBooks(page,size,connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponseDTO>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser){

        return ResponseEntity.ok(bookService.findAllBooksByOwner(page,size,connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookHistoryResponseDTO>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser){

        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page,size,connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookHistoryResponseDTO>> findAllreturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser){

        return ResponseEntity.ok(bookService.findAllReturnedBooks(page,size,connectedUser));
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("book-id")Integer bookId,
            Authentication connectedUser ){
        return ResponseEntity.ok(bookService.updateShareableStatus(bookId,connectedUser));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("book-id")Integer bookId,
            Authentication connectedUser ){
        return ResponseEntity.ok(bookService.updateArchiveStatus(bookId,connectedUser));
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable("book-id")Integer bookId,
            Authentication connectedUser){
        return ResponseEntity.ok(bookService.borrowBook(bookId,connectedUser));
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBorrowBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectUser){
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId,connectUser));
    }

    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approveReturnBorrowBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectUser) {
        return ResponseEntity.ok(bookService.approveReturnBorrowedBook(bookId, connectUser));
    }

    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("book-id") Integer bookId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectUser){
        bookService.uploadBookCoverPicture(file,connectUser,bookId);
        return ResponseEntity.accepted().build();
    }

}
