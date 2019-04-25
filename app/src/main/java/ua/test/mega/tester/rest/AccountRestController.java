package ua.test.mega.tester.rest;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import ua.test.mega.tester.core.AccountProcessor;
import ua.test.mega.tester.core.api.model.Account;
import ua.test.mega.tester.rest.api.AccountDTO;

/**
 * REST controller for managing BuildState.
 */
@RestController
@RequestMapping("/api/account/")
public class AccountRestController {

	private final Logger log = LoggerFactory.getLogger(AccountRestController.class);

	private final AccountProcessor accountProcessor;

	@Autowired
	public AccountRestController(AccountProcessor accountProcessor) {
		this.accountProcessor = accountProcessor;
	}

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<AccountDTO> getLoggedinUserAccountInfo() {
		log.info("Get account info for logged in user");

		Account account = accountProcessor.loadLoggedInUserAccountInfo();

		return Flux.just(toAccountDTO(account));
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(path = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<AccountDTO> getAccountInfo(@PathVariable long accountId) {
		log.info("Get account info for {}", accountId);

		Account account = accountProcessor.loadAccountInfo(accountId);

		return Flux.just(toAccountDTO(account));
	}

	private AccountDTO toAccountDTO(Account account) {
		return AccountDTO.builder()
				.accountId(account.getAccountId())
				.balanceInUSD(account.getBalanceInUSD())
				.build();
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(path = "/{accountId}/deposit/{amountInUSD}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BigDecimal doDeposit(@PathVariable long accountId, @PathVariable BigDecimal amountInUSD) {
		return accountProcessor.deposit(accountId, amountInUSD);
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(path = "/{accountId}/withdrawal/{amountInUSD}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BigDecimal doWithdrawal(@PathVariable long accountId, @PathVariable BigDecimal amountInUSD) {
		return accountProcessor.withdrawal(accountId, amountInUSD);
	}
}
