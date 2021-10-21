package br.com.marcello.SpringGraphQL.app.graphql.accounts;

import br.com.marcello.SpringGraphQL.app.graphql.responses.SocialMediaResponse;
import br.com.marcello.SpringGraphQL.app.graphql.responses.UserAccountResponse;
import br.com.marcello.SpringGraphQL.domain.services.SocialMediaService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class UserAccountResolver implements GraphQLResolver<UserAccountResponse> {

    private final SocialMediaService socialMediaService;

    public UserAccountResolver(SocialMediaService socialMediaService) {
        this.socialMediaService = socialMediaService;
    }

    public SocialMediaResponse getSocialMedias(UserAccountResponse userAccountResponse) {
        return socialMediaService.findByAccountUsername(userAccountResponse.getUsername());
    }

}
