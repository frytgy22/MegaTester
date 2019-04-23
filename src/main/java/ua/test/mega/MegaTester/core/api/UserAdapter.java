package ua.test.mega.MegaTester.core.api;

import java.util.Collection;

import ua.test.mega.MegaTester.core.api.model.User;

public interface UserAdapter {

	User find(String username);

	Collection<User> getAllUsers();
}
