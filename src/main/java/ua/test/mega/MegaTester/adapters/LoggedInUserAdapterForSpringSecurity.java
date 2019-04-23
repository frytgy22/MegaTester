package ua.test.mega.MegaTester.adapters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ua.test.mega.MegaTester.core.api.LoggedInUserAdapter;
import ua.test.mega.MegaTester.core.api.UserAdapter;
import ua.test.mega.MegaTester.core.api.model.User;

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

		return users.computeIfAbsent(loggedInUsername, userAdapter::find);
	}


	private String getLoggedInUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return  ((UserDetails)principal).getUsername();
		} else {
			return principal.toString();
		}
	}
}
