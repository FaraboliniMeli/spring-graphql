package br.com.marcello.SpringGraphQL.integration;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
public class UserAccountGraphQlTest extends AbstractDatabase {

    @Autowired
    GraphQLTestTemplate graphQLTemplate;

    private final String SELLER_MUTATION_FOLDER = "graphql/user-accounts/mutation/";
    private final String SELLER_QUERY_FOLDER = "graphql/user-accounts/query/";

    @Test
    public void shouldSaveUserAccount() throws IOException {
        String saveMutation = "save_account.graphql";
        GraphQLResponse response = graphQLTemplate.postForResource(SELLER_MUTATION_FOLDER + saveMutation);
        assertTrue(response.isOk());

        assertEquals("Test Account", response.get("$.data.saveAccount.username"));
        assertEquals("test@test.com", response.get("$.data.saveAccount.email"));
        assertEquals("Brazil", response.get("$.data.saveAccount.address.country"));
        assertEquals("São Paulo", response.get("$.data.saveAccount.address.state"));
        assertEquals("São Paulo", response.get("$.data.saveAccount.address.city"));
        assertEquals("Test District", response.get("$.data.saveAccount.address.district"));
        assertEquals("Test Street 10", response.get("$.data.saveAccount.address.street"));
        assertEquals("00000-000", response.get("$.data.saveAccount.address.zip_code"));

        super.clearDatabase();
    }

    @Test
    public void shouldFindTestAccount() throws IOException {
        super.persistTestValues();
        String findByUsernameQuery = "find_by_username.graphql";
        GraphQLResponse response = graphQLTemplate.postForResource(SELLER_QUERY_FOLDER + findByUsernameQuery);
        assertTrue(response.isOk());

        assertEquals("test@test.com", response.get("$.data.findAccount.email"));
        
        super.clearDatabase();
    }

    @Test
    public void shouldFindAll() throws IOException {
        super.persistTestValues();
        String findAll = "find_all.graphql";
        GraphQLResponse response = graphQLTemplate.postForResource(SELLER_QUERY_FOLDER + findAll);
        assertTrue(response.isOk());

        assertEquals("Test Account", response.get("$.data.findAllAccounts[0].username"));
        assertEquals("test@test.com", response.get("$.data.findAllAccounts[0].email"));

        super.clearDatabase();
    }

    @Test
    public void shouldUpdateOnlyNonNullValues() throws IOException {
        super.persistTestValues();
        String update = "update_account.graphql";
        String updatedAccount = "find_updated_account.graphql";
        graphQLTemplate.postForResource(SELLER_MUTATION_FOLDER + update);

        GraphQLResponse updatedResponse = graphQLTemplate.postForResource(SELLER_QUERY_FOLDER + updatedAccount);
        assertTrue(updatedResponse.isOk());

        assertEquals("New Test Account", updatedResponse.get("$.data.findAccount.username"));
        assertEquals("test@test.com", updatedResponse.get("$.data.findAccount.email"));
        assertEquals("United States", updatedResponse.get("$.data.findAccount.address.country"));
        assertEquals("São Paulo", updatedResponse.get("$.data.findAccount.address.state"));
        assertEquals("São Paulo", updatedResponse.get("$.data.findAccount.address.city"));
        assertEquals("Test District", updatedResponse.get("$.data.findAccount.address.district"));
        assertEquals("Rua Teste 10", updatedResponse.get("$.data.findAccount.address.street"));
        assertEquals("00000-000", updatedResponse.get("$.data.findAccount.address.zip_code"));

        super.clearDatabase();
    }

    @Test
    public void shouldResolveSocialMedia() throws IOException {
        super.persistTestValues();
        String resolveSocialMedia = "social_media_resolver.graphql";
        GraphQLResponse response = graphQLTemplate.postForResource(SELLER_QUERY_FOLDER + resolveSocialMedia);
        assertTrue(response.isOk());

        assertEquals("@Test", response.get("$.data.findAccount.social_medias.instagram"));
        assertEquals("Test Account", response.get("$.data.findAccount.social_medias.facebook"));
        assertEquals("@Test", response.get("$.data.findAccount.social_medias.twitter"));

        super.clearDatabase();
    }

    @Test
    public void shouldHandleAccountAlreadyExistsException() throws IOException {
        super.persistTestValues();
        String saveMutation = "save_account.graphql";
        GraphQLResponse response = graphQLTemplate.postForResource(SELLER_MUTATION_FOLDER + saveMutation);
        assertTrue(response.isOk());

        assertEquals("Account with username Test Account already exists.", response.get("$.errors[0].message"));

        super.clearDatabase();
    }

    @Test
    public void shouldHandleAccountNotFoundException() throws IOException {
        String findByUsername = "find_by_username.graphql";
        GraphQLResponse response = graphQLTemplate.postForResource(SELLER_QUERY_FOLDER + findByUsername);
        assertTrue(response.isOk());

        assertEquals("Account with username Test Account not found.", response.get("$.errors[0].message"));
    }

    @Test
    public void shouldHandleNoneSocialMediaLinkedException() throws IOException {
        String saveMutation = "save_account.graphql";
        String resolveSocialMedia = "social_media_resolver.graphql";

        graphQLTemplate.postForResource(SELLER_MUTATION_FOLDER + saveMutation);
        GraphQLResponse errorResponse = graphQLTemplate.postForResource(SELLER_QUERY_FOLDER + resolveSocialMedia);
        assertTrue(errorResponse.isOk());

        assertEquals("The account with username Test Account hasn't any Social Medias linked.",
                errorResponse.get("$.errors[0].message"));

        super.clearDatabase();
    }

}
