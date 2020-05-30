package ua.test.mega.tester.core.api;

import java.util.List;

import ua.test.mega.tester.core.api.model.Position;

public interface PositionAdapter {

	List<Position> findAll(long accountId);

	Position create(Position position);
}
