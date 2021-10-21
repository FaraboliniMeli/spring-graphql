package br.com.marcello.SpringGraphQL.domain.services.impl;

import br.com.marcello.SpringGraphQL.app.graphql.inputs.SocialMediaInput;
import br.com.marcello.SpringGraphQL.app.graphql.responses.SocialMediaResponse;
import br.com.marcello.SpringGraphQL.domain.entities.SocialMedia;
import br.com.marcello.SpringGraphQL.domain.exceptions.NoneSocialMediasLinkedException;
import br.com.marcello.SpringGraphQL.domain.repositories.SocialMediaRepository;
import br.com.marcello.SpringGraphQL.domain.services.SocialMediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SocialMediaServiceImpl implements SocialMediaService {

    private final SocialMediaRepository socialMediaRepository;
    private final MongoTemplate mongoTemplate;

    public SocialMediaServiceImpl(SocialMediaRepository socialMediaRepository, MongoTemplate mongoTemplate) {
        this.socialMediaRepository = socialMediaRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public SocialMediaResponse save(SocialMediaInput socialMediaInput) {
        SocialMedia socialMedia = this.convertInputToEntity(socialMediaInput);
        this.socialMediaRepository.save(socialMedia);

        return this.convertEntityToResponse(socialMedia);
    }

    @Override
    public List<SocialMediaResponse> findAll() {
        List<SocialMedia> socialMedias = this.socialMediaRepository.findAll();
        List<SocialMediaResponse> response = new ArrayList<>();

        socialMedias.forEach(
                socialMedia -> response.add(this.convertEntityToResponse(socialMedia))
        );

        return response;
    }

    @Override
    public SocialMediaResponse findByAccountUsername(String accountUsername) {
        SocialMedia socialMedia = this.socialMediaRepository.findSocialMediaByAccountUsername(accountUsername);
        this.verifyAccountHasSocialMediasLinked(socialMedia, accountUsername);

        return this.convertEntityToResponse(socialMedia);
    }

    @Override
    public SocialMediaResponse update(String accountUsername, SocialMediaInput fieldsToUpdate) {
        SocialMedia socialMedia = this.socialMediaRepository.findSocialMediaByAccountUsername(accountUsername);
        this.verifyAccountHasSocialMediasLinked(socialMedia, accountUsername);

        Query query = new Query();
        query.addCriteria(Criteria.where("account_username").is(accountUsername));
        Update update = new Update();

        Map<String,Object> nonNullFields = this.removeNullValuesToUpdate(fieldsToUpdate);

        nonNullFields.forEach(
                update::set
        );

        mongoTemplate.update(SocialMedia.class).matching(query).apply(update).first();

        return this.convertUpdateFieldsToResponse(nonNullFields);
    }

    @Override
    public void deleteAll() {
        this.socialMediaRepository.deleteAll();
    }

    private SocialMedia convertInputToEntity(SocialMediaInput socialMediaInput) {
        return new SocialMedia().toBuilder()
                .accountUsername(socialMediaInput.getAccountUsername())
                .facebook(socialMediaInput.getFacebook())
                .instagram(socialMediaInput.getInstagram())
                .twitter(socialMediaInput.getTwitter())
                .build();
    }

    private SocialMediaResponse convertEntityToResponse(SocialMedia socialMedia) {
        return new SocialMediaResponse().toBuilder()
                .accountUsername(socialMedia.getAccountUsername())
                .facebook(socialMedia.getFacebook())
                .instagram(socialMedia.getInstagram())
                .twitter(socialMedia.getTwitter())
                .build();
    }

    private Map<String,Object> removeNullValuesToUpdate(SocialMediaInput fieldsToUpdate) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> fieldsMap = objectMapper.convertValue(fieldsToUpdate, Map.class);
        fieldsMap.values().removeIf(Objects::isNull);

        return fieldsMap;
    }

    private SocialMediaResponse convertUpdateFieldsToResponse(Map<String, Object> nonNullFields) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(nonNullFields, SocialMediaResponse.class);
    }

    private void verifyAccountHasSocialMediasLinked(SocialMedia socialMedia, String username) {
        if (socialMedia == null)
            throw new NoneSocialMediasLinkedException(username);
    }

}
