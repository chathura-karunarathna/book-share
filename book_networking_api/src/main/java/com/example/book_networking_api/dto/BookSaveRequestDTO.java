package com.example.book_networking_api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookSaveRequestDTO {
   private Integer id;
    @NotNull(message = "100")
    @NotEmpty(message = "100")
   private String title;
    @NotNull(message = "101")
    @NotEmpty(message = "101")
   private String authorName;
    @NotNull(message = "102")
    @NotEmpty(message = "102")
   private String isbn;
    @NotNull(message = "103")
    @NotEmpty(message = "103")
   private String synopsis;
   private boolean shareable;
}
