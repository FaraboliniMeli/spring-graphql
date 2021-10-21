package br.com.marcello.SpringGraphQL.app.graphql.exceptions;

import br.com.marcello.SpringGraphQL.domain.exceptions.AccountAlreadyExistsException;
import br.com.marcello.SpringGraphQL.domain.exceptions.AccountNotFoundException;
import br.com.marcello.SpringGraphQL.domain.exceptions.NoneSocialMediasLinkedException;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
public class GraphQlExceptionHandler {

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ThrowableGraphQLError handleAccountAlreadyExistsException(AccountAlreadyExistsException e) {
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ThrowableGraphQLError handleAccountNotFoundException(AccountNotFoundException e) {
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(NoneSocialMediasLinkedException.class)
    public ThrowableGraphQLError handleNoneSocialMediasLinkedException(NoneSocialMediasLinkedException e) {
        return new ThrowableGraphQLError(e);
    }

}
