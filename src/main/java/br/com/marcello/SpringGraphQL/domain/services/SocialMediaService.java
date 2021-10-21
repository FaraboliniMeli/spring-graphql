package br.com.marcello.SpringGraphQL.domain.services;

import br.com.marcello.SpringGraphQL.app.graphql.inputs.SocialMediaInput;
import br.com.marcello.SpringGraphQL.app.graphql.responses.SocialMediaResponse;

import java.util.List;

public interface SocialMediaService {

    SocialMediaResponse save(SocialMediaInput socialMediaInput);

    List<SocialMediaResponse> findAll();

    SocialMediaResponse findByAccountUsername(String accountUsername);

    SocialMediaResponse update(String accountUsername, SocialMediaInput fieldsToUpdate);

    void deleteAll();

}
