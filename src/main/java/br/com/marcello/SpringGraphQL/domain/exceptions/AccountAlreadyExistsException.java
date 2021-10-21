package br.com.marcello.SpringGraphQL.domain.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String username) {
        super("Account with username " + username + " already exists.");
    }

}
