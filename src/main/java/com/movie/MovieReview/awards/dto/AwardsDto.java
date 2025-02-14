package com.movie.MovieReview.awards.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class AwardsDto {
    private Long awardsId;

    private String awardName;

    private Long nominated1;

    private Long nominated2;

    private Long nominated3;

    private Long nominated4;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Long topMovieId;

    private String NextAwardName;
}
