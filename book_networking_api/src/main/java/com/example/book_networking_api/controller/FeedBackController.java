package com.example.book_networking_api.controller;

import com.example.book_networking_api.dto.FeedbackRequestDto;
import com.example.book_networking_api.dto.FeedbackResponseDto;
import com.example.book_networking_api.service.FeedbackService;
import com.example.book_networking_api.utill.PageResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedBackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedbackRequestDto feedbackRequestDto,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(feedbackService.save(feedbackRequestDto, connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponseDto>> findAllFeedbackByBook(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser){

        return ResponseEntity.ok(feedbackService.findAllFeedbackByBook(bookId,page,size,connectedUser));
    }
}
