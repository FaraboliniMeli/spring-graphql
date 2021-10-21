package br.com.marcello.SpringGraphQL.unit;

import br.com.marcello.SpringGraphQL.app.graphql.inputs.SocialMediaInput;
import br.com.marcello.SpringGraphQL.domain.entities.SocialMedia;
import br.com.marcello.SpringGraphQL.domain.exceptions.NoneSocialMediasLinkedException;
import br.com.marcello.SpringGraphQL.domain.repositories.SocialMediaRepository;
import br.com.marcello.SpringGraphQL.domain.services.SocialMediaService;
import br.com.marcello.SpringGraphQL.domain.services.impl.SocialMediaServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class SocialMediaServiceTest extends UnitTest {

    @Mock
    SocialMediaRepository socialMediaRepository;

    @Mock
    MongoTemplate mongoTemplate;

    SocialMediaService socialMediaService;

    SocialMedia socialMedia;
    SocialMediaInput socialMediaInput;

    @Before
    public void setup() {
        this.socialMediaService = new SocialMediaServiceImpl(socialMediaRepository, mongoTemplate);

        socialMedia = new SocialMedia().toBuilder()
                .accountUsername("Test Account")
                .facebook("Test Account")
                .instagram("@Test")
                .twitter("@Test")
                .build();

        socialMediaInput = new SocialMediaInput().toBuilder()
                .accountUsername("Test Account")
                .facebook("Test Account")
                .instagram("@Test")
                .twitter("@Test")
                .build();
    }

    @Test
    public void shouldThrowNoneSocialMediaLinkedInFindByUsername() {
        when(socialMediaRepository.findSocialMediaByAccountUsername("Test Account")).thenReturn(null);
        assertThrows(NoneSocialMediasLinkedException.class,
                () -> socialMediaService.findByAccountUsername("Test Account"));
    }

    @Test
    public void shouldThrowNoneSocialMediaLinkedInUpdate() {
        when(socialMediaRepository.findSocialMediaByAccountUsername("Test Account")).thenReturn(null);
        assertThrows(NoneSocialMediasLinkedException.class,
                () -> socialMediaService.update("Test Account", null));
    }

}
