package br.com.marcello.SpringGraphQL.domain.exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String username) {
        super("Account with username " + username + " not found.");
    }

}
