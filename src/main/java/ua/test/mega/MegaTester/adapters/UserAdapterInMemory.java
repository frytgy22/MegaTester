package ua.test.mega.MegaTester.adapters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import ua.test.mega.MegaTester.core.api.UserAdapter;
import ua.test.mega.MegaTester.core.api.model.User;

@Component
public class UserAdapterInMemory implements UserAdapter {

	private final Map<String, User> users = new HashMap<>();

	public UserAdapterInMemory() {
		User user = User.builder()
				.username("admin")
				.password("password")
				.accountId(1)
				.roles(Arrays.asList("USER"))
				.build();

		users.put(user.getUsername(), user);
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
