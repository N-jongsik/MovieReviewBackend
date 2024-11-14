package com.movie.MovieReview.post.service;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.entity.UserPrincipal;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entitiy.Post;
import com.movie.MovieReview.post.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService{
    PostRepository postRepository;
    UserPrincipal userPrincipal;
    MemberRepository memberRepository;

    private MemberEntity getLoginMember() {
        String loginMemberEmail = userPrincipal.getEmail();
        return memberRepository.findByEmail(loginMemberEmail)
                .orElseThrow(()->new RuntimeException("member not found"));
    }

    @Override
    public Post findByPostId(Long postId) {
        try {
            return postRepository.findById(postId)
                    .orElseThrow(()->new RuntimeException("board not found with id"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error while finding post by id",e);
        }
    }

    @Override
    public PostResDto createPost(PostDto postDto) {
        String title = postDto.getTitle();
        String content = postDto.content();
        Post post = Post.builder()
                .title(title)
                .content(content)
                .build();

        postRepository.save(post);
        return PostResDto.entityToResDto(post);
    }

    @Override
    public void deletePost() {

    }

    @Override
    public PostResDto updatePost(Long postId) {
        return null;
    }

    @Override
    public List<PostResDto> findPostByMemberId(Long memberId) {
        return List.of();
    }
}
