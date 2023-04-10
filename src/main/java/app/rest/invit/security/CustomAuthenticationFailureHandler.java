package app.rest.invit.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
	        AuthenticationException exception) throws IOException, ServletException {
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    
	    String message = "";
	    
	    if (exception instanceof BadCredentialsException && exception.getMessage().equals("Bad credentials")) {
	        message = "El usuario o la contraseña no es valido";
	    } else {
	        message = exception.getMessage();
	    }
	    
	    response.getWriter().write("{\"error\": \"" + message + "\"}");
	}

}