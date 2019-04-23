package ua.test.mega.MegaTester.core.api;

import java.util.List;

import ua.test.mega.MegaTester.core.api.model.Position;

public interface PositionAdapter {

	List<Position> findAll(long accountId);

	Position create(Position position);
}
