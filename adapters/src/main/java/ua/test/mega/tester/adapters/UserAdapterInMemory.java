package ua.test.mega.tester.adapters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ua.test.mega.tester.core.api.UserAdapter;
import ua.test.mega.tester.core.api.model.User;

@Component
public class UserAdapterInMemory implements UserAdapter {

	private final Map<String, User> users = new HashMap<>();

	public UserAdapterInMemory() {

		User admin = newUser("admin", "password", -1, Arrays.asList("ADMIN"));
		User user1 = newUser("user1", "user1", 1, Arrays.asList("USER"));
		User user2 = newUser("user2", "user2", 2, Arrays.asList("USER"));

		users.put(admin.getUsername(), admin);
		users.put(user1.getUsername(), user1);
		users.put(user2.getUsername(), user2);
	}

	private User newUser(String username, String password, int accountId, List<String> roles) {
		return User.builder()
					.username(username)
					.password(password)
					.accountId(accountId)
					.roles(roles)
					.build();
	}

	@Override
	public User find(String username) {

		return users.get(username);
	}

	@Override
	public Collection<User> getAllUsers() {
		return users.values();
	}
}
