package br.com.marcello.SpringGraphQL.domain.repositories;

import br.com.marcello.SpringGraphQL.domain.entities.SocialMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SocialMediaRepository extends MongoRepository<SocialMedia, String> {

    SocialMedia findSocialMediaByAccountUsername(String accountUsername);

}
