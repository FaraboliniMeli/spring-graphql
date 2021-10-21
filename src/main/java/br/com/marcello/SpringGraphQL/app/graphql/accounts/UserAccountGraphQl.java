package br.com.marcello.SpringGraphQL.app.graphql.accounts;

import br.com.marcello.SpringGraphQL.app.graphql.inputs.UserAccountInput;
import br.com.marcello.SpringGraphQL.app.graphql.responses.UserAccountResponse;
import br.com.marcello.SpringGraphQL.domain.services.UserAccountService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAccountGraphQl implements GraphQLMutationResolver, GraphQLQueryResolver {

    private final UserAccountService userAccountService;

    public UserAccountGraphQl(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    public UserAccountResponse saveAccount(UserAccountInput userAccountInput) {
        return this.userAccountService.save(userAccountInput);
    }

    public UserAccountResponse findAccount(String username) {
        return this.userAccountService.findByUsername(username);
    }

    public UserAccountResponse deleteAccount(String username) {
        return this.userAccountService.delete(username);
    }

    public List<UserAccountResponse> findAllAccounts() {
        return this.userAccountService.findAll();
    }

    public UserAccountResponse updateAccount(String username, UserAccountInput fieldsToUpdate) {
        return this.userAccountService.update(username, fieldsToUpdate);
    }

    public void deleteAll() {
        this.userAccountService.deleteAll();
    }

}
