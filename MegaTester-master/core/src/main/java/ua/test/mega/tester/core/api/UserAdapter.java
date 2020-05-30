package ua.test.mega.tester.core.api;

import java.util.Collection;

import ua.test.mega.tester.core.api.model.User;

public interface UserAdapter {

	User find(String username);

	Collection<User> getAllUsers();
}
