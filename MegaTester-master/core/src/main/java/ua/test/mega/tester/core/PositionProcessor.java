package ua.test.mega.tester.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.test.mega.tester.core.api.LoggedInUserAdapter;
import ua.test.mega.tester.core.api.PositionAdapter;
import ua.test.mega.tester.core.api.model.Position;

@Component
public class PositionProcessor {

	private LoggedInUserAdapter loggedInUserAdapter;
	private PositionAdapter positionAdapter;

	@Autowired
	public PositionProcessor(LoggedInUserAdapter loggedInUserAdapter, PositionAdapter positionAdapter) {
		this.loggedInUserAdapter = loggedInUserAdapter;
		this.positionAdapter = positionAdapter;
	}

	public List<Position> findPositions() {
		long accountId = loggedInUserAdapter.getLoggedInUser().getAccountId();

		return positionAdapter.findAll(accountId);
	}
}
