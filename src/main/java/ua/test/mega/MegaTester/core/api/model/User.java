package ua.test.mega.MegaTester.core.api.model;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class User {

	@NonNull
	private final String username;
	@NonNull
	private final String password;
	@NonNull
	private final List<String> roles;

	private final long accountId;
}
