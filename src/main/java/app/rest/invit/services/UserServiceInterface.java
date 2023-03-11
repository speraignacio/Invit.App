package app.rest.invit.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import app.rest.invit.dto.PostDto;
import app.rest.invit.dto.UserDto;

public interface UserServiceInterface extends UserDetailsService {
    public UserDto createUser(UserDto user);

    public UserDto getUser(String email);

    public List<PostDto> getUserPosts(String email);
}