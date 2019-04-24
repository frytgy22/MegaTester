package ua.test.mega.tester.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.test.mega.tester.core.PositionProcessor;
import ua.test.mega.tester.core.api.model.Position;
import ua.test.mega.tester.rest.api.PositionDTO;

@RestController
@RequestMapping("/api/position/")
public class PositionRestController {

	private final PositionProcessor orderProcessor;

	@Autowired
	public PositionRestController(PositionProcessor positionProcessor) {
		this.orderProcessor = positionProcessor;
	}

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PositionDTO> getOrders() {
		List<Position> positions = orderProcessor.findPositions();

		return positions.stream()
				.map(this::toPositionDTO)
				.collect(Collectors.toList());
	}

	private PositionDTO toPositionDTO(Position position) {
		return PositionDTO.builder()
				.positionId(position.getPositionId())
				.accountId(position.getAccountId())
				.orderId(position.getOrderId())
				.executionDate(position.getExecutionDate())
				.priceInUSD(position.getPriceInUSD())
				.build();
	}
}
