package ua.test.mega.MegaTester.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ua.test.mega.MegaTester.core.api.PositionAdapter;
import ua.test.mega.MegaTester.core.api.model.Position;

@Component
public class PositionAdapterInMemory implements PositionAdapter {

	private AtomicLong idGenerator = new AtomicLong();
	private Map<Long, List<Position>> positions = new HashMap<>();

	@Override
	public List<Position> findAll(long accountId) {
		return positions.values().stream()
				.flatMap(positions -> positions.stream())
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
