package app.rest.invit.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.rest.invit.repositories.EventRepository;
import app.rest.invit.repositories.UserRepository;
import app.rest.invit.entities.EventEntity;
import app.rest.invit.entities.UserEntity;
import app.rest.invit.exceptions.EmailExistsException;
import app.rest.invit.dto.EventDto;
import app.rest.invit.dto.UserDto;

@Service
public class UserService implements UserServiceInterface {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EventRepository eventRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	ModelMapper mapper;

	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null)
			throw new EmailExistsException("El correo electronico ya existe");

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		UUID userId = UUID.randomUUID();
		userEntity.setUserId(userId.toString());
//        userEntity.setUserState(1);
		try {
			UserService.enviarCorreo(userEntity);
		} catch (Exception e) {
			throw new EmailExistsException(e + "No se pudo enviar el correo");
		}

		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDto userToReturn = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, userToReturn);

		return userToReturn;
	}

	@Override
	public UserDto checkEmail(UserDto user) {

		UserEntity userEntity = new UserEntity();
		
		BeanUtils.copyProperties(user, userEntity);

		userEntity.setUserState(1);

		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDto userToReturn = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, userToReturn);

		return userToReturn;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}

		UserDto userToReturn = new UserDto();

		BeanUtils.copyProperties(userEntity, userToReturn);

		return userToReturn;
	}
	
	@Override
	public UserDto getUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserID(userId);

		if (userEntity == null) {
			throw new UsernameNotFoundException("No existe usuario");
		}

		UserDto userToReturn = new UserDto();

		BeanUtils.copyProperties(userEntity, userToReturn);

		return userToReturn;
	}
	
	
	@Override
	public List<EventDto> getUserEvents(String email) {

		UserEntity userEntity = userRepository.findByEmail(email);

		List<EventEntity> events = eventRepository.getByUserIdOrderByCreatedAtDesc(userEntity.getId());

		List<EventDto> eventDtos = new ArrayList<>();

		for (EventEntity event : events) {
			EventDto eventDto = mapper.map(event, EventDto.class);
			eventDtos.add(eventDto);
		}

		return eventDtos;
	}

	// Método para enviar un correo electrónico
	public static void enviarCorreo(UserEntity userEntity) throws Exception {

		// Configuración del servidor SMTP
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		// Autenticación del remitente
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("info.invit.app@gmail.com", "wzip yipu dldw keoi");
			}
		});

		// Creación del mensaje de correo electrónico
		Message message = new MimeMessage(session);
		String enlace = "http://localhost:3000/confirmarCuenta/" + userEntity.getUserId();
		message.setFrom(new InternetAddress("info.invit.app@gmail.com"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEntity.getEmail()));
		message.setSubject("Confirmación de alta de usuario");
		message.setText("Para confirmar su alta, por favor haga clic en el siguiente enlace: " + enlace);

		// Envío del mensaje de correo electrónico
		Transport.send(message);

		System.out.println("Correo electrónico enviado exitosamente.");
	}

}
