package com.samsung3.quangbatourdulich.dto.respone;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDTO {
    private Integer reviewId;
    private Integer tourId;
    private Integer userId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}