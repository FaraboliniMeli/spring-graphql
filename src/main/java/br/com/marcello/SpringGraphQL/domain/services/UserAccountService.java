package br.com.marcello.SpringGraphQL.domain.services;

import br.com.marcello.SpringGraphQL.app.graphql.inputs.UserAccountInput;
import br.com.marcello.SpringGraphQL.app.graphql.responses.UserAccountResponse;

import java.util.List;

public interface UserAccountService {

    UserAccountResponse save(UserAccountInput userAccountInput);

    List<UserAccountResponse> findAll();

    UserAccountResponse findByUsername(String username);

    UserAccountResponse update(String id, UserAccountInput fieldsToUpdate);

    UserAccountResponse delete(String username);

    void deleteAll();

}
