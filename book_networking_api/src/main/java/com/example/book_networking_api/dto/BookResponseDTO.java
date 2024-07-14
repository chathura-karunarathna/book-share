package com.example.book_networking_api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookResponseDTO {

   private Integer id;
   private String title;
   private String authorName;
   private String isbn;
   private String synopsis;
   private String owner;
   private byte[] cover;
   private double rate;
   private boolean archived;
   private boolean shareable;
}
