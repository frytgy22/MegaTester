package ua.test.mega.tester.adapters;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ua.test.mega.tester.core.api.PositionAdapter;
import ua.test.mega.tester.core.api.model.Position;

@Component
public class PositionAdapterInMemory implements PositionAdapter {

	private AtomicLong idGenerator = new AtomicLong();
	private Map<Long, List<Position>> positions = new HashMap<>();

	@Override
	public List<Position> findAll(long accountId) {
		return positions.values().stream()
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	@Override
	public Position create(Position position) {
		Objects.requireNonNull(position);

		Position positionForSave = position.toBuilder()
				.positionId(idGenerator.incrementAndGet())
				.build();

		positions.computeIfAbsent(position.getAccountId(), accountId -> new ArrayList<>())
				.add(positionForSave);

		return positionForSave;
	}

}
