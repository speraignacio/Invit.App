package app.rest.invit.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.rest.invit.dto.EventDto;
import app.rest.invit.dto.UserDto;
import app.rest.invit.requests.UserDetailsRequestModel;
import app.rest.invit.requests.UserIdRequestModel;
import app.rest.invit.responses.EventRest;
import app.rest.invit.responses.UserRest;
import app.rest.invit.services.UserServiceInterface;

@RestController
@RequestMapping("/users")
public class UserControllers {

	@Autowired
	UserServiceInterface userService;

	@Autowired
	ModelMapper mapper;

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getPrincipal().toString();

		UserDto userDto = userService.getUser(email);

		UserRest userToReturn = mapper.map(userDto, UserRest.class);

		return userToReturn;
	}

	@PostMapping
	public UserRest createUser(@RequestBody @Valid UserDetailsRequestModel userDetails) {

		UserRest userToReturn = new UserRest();

		UserDto userDto = new UserDto();

		BeanUtils.copyProperties(userDetails, userDto);

		UserDto createdUser = userService.createUser(userDto);

		BeanUtils.copyProperties(createdUser, userToReturn);

		return userToReturn;
	}
	
	@PostMapping("/checkMail")
	public UserRest checkMail(@RequestBody @Valid UserIdRequestModel userId) {

//		UserRest userToReturn = new UserRest();

		UserDto userDto = userService.getUserId(userId.getIdUser());

        UserRest userRest = mapper.map(userDto, UserRest.class);

		UserDto createdUser = userService.checkEmail(userDto);

		BeanUtils.copyProperties(createdUser, userRest);

		return userRest;
	}

	@GetMapping(path = "/events") // localhost:8080/users/events
	public List<EventRest> getEvents() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getPrincipal().toString();

		List<EventDto> events = userService.getUserEvents(email);

		List<EventRest> eventRests = new ArrayList<>();

		for (EventDto event : events) {
			EventRest eventRest = mapper.map(event, EventRest.class);
			if (eventRest.getExpiresAt().compareTo(new Date(System.currentTimeMillis())) < 0) {
				eventRest.setExpired(true);
			}
			eventRests.add(eventRest);
		}

		return eventRests;
	}
}
