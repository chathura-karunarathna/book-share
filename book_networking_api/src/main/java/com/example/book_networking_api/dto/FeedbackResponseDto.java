package com.example.book_networking_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FeedbackResponseDto {

    private Double note;
    private String comment;
    private boolean ownFeedback;
}
