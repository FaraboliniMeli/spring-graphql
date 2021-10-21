package br.com.marcello.SpringGraphQL.integration;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SocialMediaGraphQlTest extends AbstractDatabase {

    @Autowired
    GraphQLTestTemplate graphQLTemplate;

    private final String SOCIAL_MEDIA_MUTATION_FOLDER = "graphql/social-medias/mutation/";
    private final String SOCIAL_MEDIA_QUERY_FOLDER = "graphql/social-medias/query/";

    @Test
    public void shouldSaveSocialMedias() throws IOException {
        String saveMutation = "save_social_media.graphql";
        GraphQLResponse response = graphQLTemplate.postForResource(SOCIAL_MEDIA_MUTATION_FOLDER + saveMutation);
        assertTrue(response.isOk());

        assertEquals("Test Account", response.get("$.data.save.account_username"));
        assertEquals("@Test", response.get("$.data.save.instagram"));
        assertEquals("Test Account", response.get("$.data.save.facebook"));
        assertEquals("@Test", response.get("$.data.save.twitter"));

        super.clearDatabase();
    }

    @Test
    public void shouldFindSocialMediasByAccountUsername() throws IOException {
        super.persistTestValues();

        String findByUsername = "find_by_username.graphql";
        GraphQLResponse response = graphQLTemplate.postForResource(SOCIAL_MEDIA_QUERY_FOLDER + findByUsername);
        assertTrue(response.isOk());

        assertEquals("@Test", response.get("$.data.findSocialMedia.instagram"));
        assertEquals("Test Account", response.get("$.data.findSocialMedia.facebook"));
        assertEquals("@Test", response.get("$.data.findSocialMedia.twitter"));

        super.clearDatabase();
    }

    @Test
    public void shouldFindAll() throws IOException {
        super.persistTestValues();

        String saveMutation = "find_all.graphql";
        GraphQLResponse response = graphQLTemplate.postForResource(SOCIAL_MEDIA_QUERY_FOLDER + saveMutation);
        assertTrue(response.isOk());

        assertEquals("Test Account", response.get("$.data.findAll[0].account_username"));
        assertEquals("@Test", response.get("$.data.findAll[0].instagram"));
        assertEquals("Test Account", response.get("$.data.findAll[0].facebook"));
        assertEquals("@Test", response.get("$.data.findAll[0].twitter"));

        super.clearDatabase();
    }

    @Test
    public void shouldOnlyUpdateNonNullValues() throws IOException {
        super.persistTestValues();
        String update = "update_social_media.graphql";
        String updatedSocialMedias = "find_by_username.graphql";
        GraphQLResponse response = graphQLTemplate.postForResource(SOCIAL_MEDIA_MUTATION_FOLDER + update);
        assertTrue(response.isOk());

        GraphQLResponse updatedResponse = graphQLTemplate.postForResource(SOCIAL_MEDIA_QUERY_FOLDER + updatedSocialMedias);

        assertEquals("@NewTest", updatedResponse.get("$.data.findSocialMedia.instagram"));
        assertEquals("Test Account", updatedResponse.get("$.data.findSocialMedia.facebook"));
        assertEquals("@Test", updatedResponse.get("$.data.findSocialMedia.twitter"));

        super.clearDatabase();
    }

}
