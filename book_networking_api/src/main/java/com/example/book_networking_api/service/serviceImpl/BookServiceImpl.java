package com.example.book_networking_api.service.serviceImpl;

import com.example.book_networking_api.dto.BookResponseDTO;
import com.example.book_networking_api.dto.BookSaveRequestDTO;
import com.example.book_networking_api.dto.BookSpecification;
import com.example.book_networking_api.dto.BorrowedBookHistoryResponseDTO;
import com.example.book_networking_api.entity.Book;
import com.example.book_networking_api.entity.BookTransactionHistory;
import com.example.book_networking_api.entity.User;
import com.example.book_networking_api.exception.OperationNotPermittedException;
import com.example.book_networking_api.repo.BookRepo;
import com.example.book_networking_api.repo.BookTransactionHistoryRepo;
import com.example.book_networking_api.service.BookMapper;
import com.example.book_networking_api.service.BookService;
import com.example.book_networking_api.service.FileStorageService;
import com.example.book_networking_api.utill.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookRepo bookRepo;
    private final BookTransactionHistoryRepo bookTransactionHistoryRepo;
    private final FileStorageService fileStorageService;

    @Override
    public Integer save(BookSaveRequestDTO bookSaveRequestDTO, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book =  bookMapper.toBook(bookSaveRequestDTO);
        book.setOwner(user);
        return bookRepo.save(book).getId();
    }

    @Override
    public BookResponseDTO findByID(Integer bookId) {
        return bookRepo.findById(bookId).map(bookMapper::toBooKResponseDTO)
                .orElseThrow(()-> new EntityNotFoundException("No book found with the ID : "+ bookId));
    }

    @Override
    public PageResponse<BookResponseDTO> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page,size);
        Page<Book> books = bookRepo.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponseDTO> bookResponseDTOS = books.stream().map(bookMapper::toBooKResponseDTO).toList();
        return new PageResponse<>(bookResponseDTOS,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BookResponseDTO> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepo.findAll(BookSpecification.withOwnerId(user.getId()), pageable);
        List<BookResponseDTO> bookResponseDTOS = books.stream().map(bookMapper::toBooKResponseDTO).toList();
        return new PageResponse<>(bookResponseDTOS,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookHistoryResponseDTO> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepo.findAllBorrowedBooks(user.getId(),pageable);
        List<BorrowedBookHistoryResponseDTO> borrowedBookHistoryResponseDTOS = allBorrowedBooks.stream().map(bookMapper::toBorrowedBookHistoryResponseDTO).toList();
        return new PageResponse<>(borrowedBookHistoryResponseDTOS,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookHistoryResponseDTO> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepo.findAllReturnedBooks(user.getId(),pageable);
        List<BorrowedBookHistoryResponseDTO> borrowedBookHistoryResponseDTOS = allBorrowedBooks.stream().map(bookMapper::toBorrowedBookHistoryResponseDTO).toList();
        return new PageResponse<>(borrowedBookHistoryResponseDTOS,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    @Override
    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepo.findById(bookId).orElseThrow(()->new EntityNotFoundException("No book found with ID : "+ bookId));
        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot update books shareable status");
        }
        book.setShareable(!book.isShareable());
        bookRepo.save(book);
        return bookId;
    }

    @Override
    public Integer updateArchiveStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepo.findById(bookId).orElseThrow(()->new EntityNotFoundException("No book found with ID : "+ bookId));
        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot update books archive status");
        }
        book.setArchived(!book.isArchived());
        bookRepo.save(book);
        return bookId;
    }

    @Override
    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepo.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with the ID : " + bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("the requested book cannot be borrowed since book is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot borrow your own book.");
        }
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepo.isAlreadyBorrowedByUser(bookId, user.getId());
        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("The request book is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepo.save(bookTransactionHistory).getId();
    }

    @Override
    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepo.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with the ID : " + bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("the requested book cannot be borrowed since book is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot borrow or return your own book.");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepo.findBookIdAndUserId(bookId,user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book."));
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepo.save(bookTransactionHistory).getId();
    }

    @Override
    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectUser) {
        Book book = bookRepo.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with the ID : " + bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("the requested book cannot be borrowed since book is archived or not shareable");
        }
        User user = ((User) connectUser.getPrincipal());
        if(!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot return a book that you do not own.");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepo.findBookIdAndOwnerId(bookId,user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. You cannot approve return."));
        bookTransactionHistory.setReturnApproved(true);
        return bookTransactionHistoryRepo.save(bookTransactionHistory).getId();
    }

    @Override
    public void uploadBookCoverPicture(MultipartFile file, Authentication connectUser, Integer bookId) {
        Book book = bookRepo.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with the ID : " + bookId));
        User user = ((User) connectUser.getPrincipal());
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepo.save(book);
    }

}
