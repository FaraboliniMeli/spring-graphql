package br.com.marcello.SpringGraphQL.app.graphql.social_medias;

import br.com.marcello.SpringGraphQL.app.graphql.inputs.SocialMediaInput;
import br.com.marcello.SpringGraphQL.app.graphql.responses.SocialMediaResponse;
import br.com.marcello.SpringGraphQL.domain.services.SocialMediaService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SocialMediaGraphQl implements GraphQLMutationResolver, GraphQLQueryResolver {

    private final SocialMediaService socialMediaService;

    public SocialMediaGraphQl(SocialMediaService socialMediaService) {
        this.socialMediaService = socialMediaService;
    }

    public SocialMediaResponse save(SocialMediaInput socialMediaInput) {
        return this.socialMediaService.save(socialMediaInput);
    }

    public List<SocialMediaResponse> findAll() {
        return this.socialMediaService.findAll();
    }

    public SocialMediaResponse findSocialMedia(String username) {
        return this.socialMediaService.findByAccountUsername(username);
    }

    public SocialMediaResponse update(String username, SocialMediaInput socialMediaInput) {
        return this.socialMediaService.update(username, socialMediaInput);
    }

    public void deleteAllSocialMedias() {
        this.socialMediaService.deleteAll();
    }

}
