package com.example.book_networking_api.service;

import com.example.book_networking_api.dto.FeedbackRequestDto;
import com.example.book_networking_api.dto.FeedbackResponseDto;
import com.example.book_networking_api.utill.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Integer save(FeedbackRequestDto feedbackRequestDto, Authentication connectedUser);

    PageResponse<FeedbackResponseDto> findAllFeedbackByBook(Integer bookId, int page, int size, Authentication connectedUser);
}
