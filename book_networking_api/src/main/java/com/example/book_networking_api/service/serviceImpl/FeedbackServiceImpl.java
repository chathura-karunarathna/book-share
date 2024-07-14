package com.example.book_networking_api.service.serviceImpl;

import com.example.book_networking_api.dto.FeedbackRequestDto;
import com.example.book_networking_api.dto.FeedbackResponseDto;
import com.example.book_networking_api.entity.Book;
import com.example.book_networking_api.entity.FeedBack;
import com.example.book_networking_api.entity.User;
import com.example.book_networking_api.exception.OperationNotPermittedException;
import com.example.book_networking_api.repo.BookRepo;
import com.example.book_networking_api.repo.FeedbackRepo;
import com.example.book_networking_api.service.FeedbackMapper;
import com.example.book_networking_api.service.FeedbackService;
import com.example.book_networking_api.utill.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final BookRepo bookRepo;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepo feedbackRepo;

    @Override
    public Integer save(FeedbackRequestDto feedbackRequestDto, Authentication connectedUser) {
        Book book = bookRepo.findById(feedbackRequestDto.getBookId()).orElseThrow(()-> new EntityNotFoundException("No book found with the ID : " + feedbackRequestDto.getBookId()));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You cannot give feedback for an archived or not shareable books");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot give a feedback to your own book");
        }
        FeedBack feedBack= feedbackMapper.toFeedBack(feedbackRequestDto);
        return feedbackRepo.save(feedBack).getId();
    }

    @Override
    public PageResponse<FeedbackResponseDto> findAllFeedbackByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page,size);
        User user = ((User) connectedUser.getPrincipal());
        Page<FeedBack> feedbacks = feedbackRepo.findAllByBookId(bookId,pageable);
        List<FeedbackResponseDto> feedbackResponseDtoList = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedBackResponseDTO(f, user.getId()))
                .toList();

        return new PageResponse<>(
                feedbackResponseDtoList,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
