package app.rest.invit.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import app.rest.invit.dto.EventDto;
import app.rest.invit.dto.UserDto;
import app.rest.invit.entities.UserEntity;

public interface UserServiceInterface extends UserDetailsService {
    public UserDto createUser(UserDto user);

    public UserDto getUser(String email);

    public List<EventDto> getUserEvents(String email);

	public UserDto checkEmail(UserDto user);

	public UserDto getUserId(String userId);
}
