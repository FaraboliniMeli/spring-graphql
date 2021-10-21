package br.com.marcello.SpringGraphQL.unit;

import br.com.marcello.SpringGraphQL.app.graphql.inputs.UserAccountInput;
import br.com.marcello.SpringGraphQL.app.graphql.responses.UserAccountResponse;
import br.com.marcello.SpringGraphQL.domain.entities.Address;
import br.com.marcello.SpringGraphQL.domain.entities.UserAccount;
import br.com.marcello.SpringGraphQL.domain.exceptions.AccountAlreadyExistsException;
import br.com.marcello.SpringGraphQL.domain.exceptions.AccountNotFoundException;
import br.com.marcello.SpringGraphQL.domain.repositories.UserAccountRepository;
import br.com.marcello.SpringGraphQL.domain.services.UserAccountService;
import br.com.marcello.SpringGraphQL.domain.services.impl.UserAccountServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserAccountServiceTest extends UnitTest {

    @Mock
    UserAccountRepository userAccountRepository;

    @Mock
    MongoTemplate mongoTemplate;

    UserAccountService userAccountService;

    UserAccount userAccount;
    UserAccountInput userAccountInput;
    Address address;
    List<UserAccount> response;

    @Before
    public void setup() {
        this.userAccountService = new UserAccountServiceImpl(userAccountRepository, mongoTemplate);

        address = new Address().toBuilder()
                .country("Brazil")
                .state("São Paulo")
                .city("São Paulo")
                .district("Test")
                .street("Test Street 10")
                .zipCode("00000-000")
                .build();

        userAccount = new UserAccount().toBuilder()
                .username("Test Account")
                .email("test@test.com")
                .address(address)
                .build();

        userAccountInput = new UserAccountInput()
                .toBuilder()
                .username("Test Account")
                .email("test@test.com")
                .address(address)
                .build();

        response = new ArrayList<>();
        response.add(userAccount);
    }

    @Test
    public void shouldThrowAccountAlreadyExistsExceptionInSave() {
        when(userAccountRepository.findUserAccountByUsername("Test Account")).thenReturn(userAccount);
        assertThrows(AccountAlreadyExistsException.class,
                () -> this.userAccountService.save(userAccountInput));
    }

    @Test
    public void shouldThrowAccountNotFoundExceptionInFindByUsername() {
        when(userAccountRepository.findUserAccountByUsername("Test Account")).thenReturn(null);
        assertThrows(AccountNotFoundException.class,
                () -> this.userAccountService.findByUsername("Test Account"));
    }

    @Test
    public void shouldThrowAccountNotFoundExceptionInUpdate() {
        when(userAccountRepository.findUserAccountByUsername("Test Account")).thenReturn(null);
        assertThrows(AccountNotFoundException.class,
                () -> this.userAccountService.update("Test Account", null));
    }

    @Test
    public void shouldThrowAccountNotFoundExceptionInDelete() {
        when(userAccountRepository.findUserAccountByUsername("Test Account")).thenReturn(null);
        assertThrows(AccountNotFoundException.class,
                () -> this.userAccountService.delete("Test Account"));
    }

    @Test
    public void shouldReturnUserAccountResponseList() {
        when(userAccountRepository.findAll()).thenReturn(response);
        List<UserAccountResponse> serviceResponse = this.userAccountService.findAll();
        assertNotNull(serviceResponse);
    }

    @Test
    public void shouldFindByUsername() {
        when(userAccountRepository.findUserAccountByUsername("Test Account")).thenReturn(userAccount);
        UserAccountResponse response = this.userAccountService.findByUsername("Test Account");
        assertEquals("Test Account", response.getUsername());
    }

}
