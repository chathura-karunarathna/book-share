package com.example.book_networking_api.service;

import com.example.book_networking_api.dto.FeedbackRequestDto;
import com.example.book_networking_api.dto.FeedbackResponseDto;
import com.example.book_networking_api.entity.Book;
import com.example.book_networking_api.entity.FeedBack;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public FeedBack toFeedBack(FeedbackRequestDto feedbackRequestDto) {
        return FeedBack.builder()
                .note(feedbackRequestDto.getNote())
                .comment((feedbackRequestDto.getComment()))
                .book(Book.builder()
                        .id(feedbackRequestDto.getBookId())
                        .archived(false)
                        .shareable(false)
                        .build()
                )
                .build();
    }

    public FeedbackResponseDto toFeedBackResponseDTO(FeedBack feedBack, Integer id) {
        return FeedbackResponseDto.builder()
                .note(feedBack.getNote())
                .comment(feedBack.getComment())
                .ownFeedback(Objects.equals(feedBack.getCreatedBy(),id))
                .build();
    }
}
