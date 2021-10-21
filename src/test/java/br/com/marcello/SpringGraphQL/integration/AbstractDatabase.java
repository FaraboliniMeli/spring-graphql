package br.com.marcello.SpringGraphQL.integration;

import br.com.marcello.SpringGraphQL.domain.entities.Address;
import br.com.marcello.SpringGraphQL.domain.entities.SocialMedia;
import br.com.marcello.SpringGraphQL.domain.entities.UserAccount;
import br.com.marcello.SpringGraphQL.domain.repositories.SocialMediaRepository;
import br.com.marcello.SpringGraphQL.domain.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;


public abstract class AbstractDatabase extends GraphQLIntegrationTest {

    static final MongoDBContainer mongoDB;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    SocialMediaRepository socialMediaRepository;

    static {
        mongoDB = new MongoDBContainer("mongo:latest")
        .withExposedPorts(27017);
        mongoDB.start();
    }

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDB::getReplicaSetUrl);
    }

    void persistTestValues() {
        Address testAddress = new Address().toBuilder()
                .country("Brazil")
                .state("São Paulo")
                .city("São Paulo")
                .district("Test District")
                .street("Test Street 10")
                .zipCode("00000-000")
                .build();

        UserAccount testAccount = new UserAccount().toBuilder()
                .username("Test Account")
                .email("test@test.com")
                .address(testAddress)
                .build();

        SocialMedia testSocialMedia = new SocialMedia().toBuilder()
                .accountUsername("Test Account")
                .instagram("@Test")
                .twitter("@Test")
                .facebook("Test Account")
                .build();

        userAccountRepository.save(testAccount);
        socialMediaRepository.save(testSocialMedia);
    }

    void clearDatabase() {
        userAccountRepository.deleteAll();
        socialMediaRepository.deleteAll();
    }

}
