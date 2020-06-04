package ua.test.mega.tester.adapters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.ToString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.UserAdapter;
import ua.test.mega.tester.core.api.model.User;
import ua.test.mega.tester.core.exceptions.UnauthorizedRequestException;

@ToString
@Component
public class LoggedInUserAdapterForSpringSecurity implements LoggedInUserAdapter {

	private final UserAdapter userAdapter;
	private final Map<String, User> users = new ConcurrentHashMap<>();

	@Autowired
	public LoggedInUserAdapterForSpringSecurity(UserAdapter userAdapter) {
		this.userAdapter = userAdapter;
	}

	@Override
	public User getLoggedInUser() {
		String loggedInUsername = getLoggedInUsername();

		if (null == loggedInUsername) {
			throw new UnauthorizedRequestException();
		}

		return users.computeIfAbsent(loggedInUsername, userAdapter::find);
	}

	private String getLoggedInUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
		}
	}
}
