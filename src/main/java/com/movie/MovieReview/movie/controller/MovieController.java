package com.movie.MovieReview.movie.controller;

import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.service.MovieRecommendService;
import com.movie.MovieReview.movie.service.MovieService;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.MovieReview.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/movie")
@CrossOrigin("*")
@Log4j2
public class MovieController {

    private final MovieService movieService;
    private final MovieRecommendService movieRecommendService;
    private final ReviewService reviewService;
    private final JwtTokenService jwtTokenService;

    //JWTToken에서 memberId추출
    private Long extractMemberId(String authorizationHeader) throws Exception {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("클라이언트에서 헤더 토큰 오류!!!!!");
        }

        String token = authorizationHeader.substring(7); // JWT 토큰 뽑아내기
        if (!jwtTokenService.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰!!!!");
        }

        return Long.valueOf(jwtTokenService.getPayload(token));
    }

    //MainPage
    @GetMapping("/topRated") //topRated영화 가져오기
    public ResponseEntity<?> getTopRatedMovies(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 토큰에서 memberId 추출
            Long memberId = extractMemberId(authorizationHeader);
            log.info("MovieController 멤버 아이디 출력: " + memberId);

            List<MovieCardDto> result = movieService.getTopRatedMovies(memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반환 값 오류!!!!");
        }
    }

    @GetMapping("/nowPlaying") //nowPlaying영화 가져오기
    public ResponseEntity<?> getNowPlayingMovies(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 토큰에서 memberId 추출
            Long memberId = extractMemberId(authorizationHeader);
            log.info("MovieController 멤버 아이디 출력: " + memberId);

            List<MovieCardDto> result = movieService.getNowPlayingMovies(memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반환 값 오류!!!!");
        }
    }

    @GetMapping("/upComing") //upComing영화 가져오기
    public ResponseEntity<?> getUpComingMovies(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 토큰에서 memberId 추출
            Long memberId = extractMemberId(authorizationHeader);
            log.info("MovieController 멤버 아이디 출력: " + memberId);

            List<MovieCardDto> result = movieService.getUpComingMovies(memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반환 값 오류!!!!");
        }
    }

    @GetMapping("/popular") //popular영화 가져오기
    public ResponseEntity<?> getPopularMovies(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 토큰에서 memberId 추출
            Long memberId = extractMemberId(authorizationHeader);
            log.info("MovieController 멤버 아이디 출력: " + memberId);

            List<MovieCardDto> result = movieService.getPopularMovies(memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반환 값 오류!!!!");
        }
    }

    @GetMapping("/recommendations") //~~를 보셨다면?? -> 랜덤으로 하나 골라서 추천해주는 거
    public ResponseEntity<?> getMemberRecommendByMemberId(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 토큰에서 memberId 추출
            Long memberId = extractMemberId(authorizationHeader);
            log.info("MovieController 멤버 아이디 출력: " + memberId);

            List<MovieCardDto> result = movieService.getMovieMemberRecommendations(memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반환 값 오류!!!!");
        }
    }

    //MovieDetailPage
    @GetMapping("/relatedContents/{id}") //상세페이지 관련 콘텐츠 가져오기
    public ResponseEntity<?> getRecommendMovies(@PathVariable("id") Long id ,@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 토큰에서 memberId 추출
            Long memberId = extractMemberId(authorizationHeader);
            log.info("MovieController 멤버 아이디 출력: " + memberId);

            List<MovieCardDto> result = movieService.getRecommendMovies(id, memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반환 값 오류!!!!");
        }
    }

    //DB에 저장하는 용도
    @GetMapping("/SaveTopRatedId") //topRated 영화들 id값만 db에 저장
    public List<Long> SaveTopRatedId(){
        try{
            return movieService.SaveTopRatedId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @GetMapping("/{id}") //영화 상세정보 tmdb에서 가져옴
//    public MovieDetailsDto getMovieDetails(@PathVariable ("id") Long id) {
//        try{
//            log.info("MovieController: 영화아이디 값은?" + id);
//            return movieService.getMovieDetails(id);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @GetMapping("/topRatedDetails") //DB에 저장된 id바탕으로 상세정보 저장
    public List<MovieDetailsDto> getTopRatedMovieDetails() {
        try {
            return movieService.getTopRatedMovieDetails();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    //누르면 상세페이지로 감
    @GetMapping("/{id}") //우리 DB에서 영화 상세정보 검색
    public ResponseEntity<?> getTopRatedMovieDetailsInDB(@PathVariable ("id") Long id, @RequestHeader("Authorization") String authorizationHeader) {
        try{
            // 토큰에서 memberId 추출
            Long memberId = extractMemberId(authorizationHeader);
            log.info("MovieController 멤버 아이디 출력: " + memberId);
            MovieDetailsDto result = movieService.getTopRatedMovieDetailsInDB(id, memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @GetMapping("/search/{name}") //영화 제목으로 검색
//    public MovieDetailsDto searchMovie(@PathVariable ("name") String name){
//        try{
//            return movieService.searchMovie(name);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @GetMapping("/search")
//    public List<MovieCardDto> search(@RequestParam String query) {
//        return movieService.searchByQuery(query); // 이름에 query가 포함된 제품을 검색
//    }

    //검색 기능
    @GetMapping("/search/{keyword}") //키워드 포함되어 있는 거 모두 검색 with 페이지네이션
    public ResponseEntity<PageResponseDto<MovieCardDto>> getKeywordResult(
            @PathVariable("keyword") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){

        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(page)
                .size(size)
                .build();
        PageResponseDto<MovieCardDto> response = movieService.getAllMovieByKeyword(keyword, pageRequestDto);

        return ResponseEntity.ok(response);
    }
}

