package br.com.marcello.SpringGraphQL.domain.repositories;

import br.com.marcello.SpringGraphQL.domain.entities.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    UserAccount findUserAccountByUsername(String username);

    void deleteUserAccountByUsername(String username);

}
