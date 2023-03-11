package app.rest.invit.services;

import java.util.List;

import app.rest.invit.dto.PostCreationDto;
import app.rest.invit.dto.PostDto;

public interface PostServiceInterface {
    public PostDto createPost(PostCreationDto post);

    public List<PostDto> getLastPosts();

    public PostDto getPost(String postId);

    public void deletePost(String postId, long userId);

    public PostDto updatePost(String postId, long userId, PostCreationDto postUpdateDto);
}
