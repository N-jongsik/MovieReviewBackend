package com.movie.MovieReview.post.service;

import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entitiy.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    Post findByPostId(Long postId);
    PostResDto createPost(PostDto postDto);
    void deletePost(Long postId);
    PostResDto updatePost(Long postId);
    List<PostResDto> findPostByMemberId(Long memberId);
}
