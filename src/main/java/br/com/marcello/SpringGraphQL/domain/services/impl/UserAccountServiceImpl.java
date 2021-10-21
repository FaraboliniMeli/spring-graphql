package br.com.marcello.SpringGraphQL.domain.services.impl;

import br.com.marcello.SpringGraphQL.app.graphql.inputs.UserAccountInput;
import br.com.marcello.SpringGraphQL.app.graphql.responses.UserAccountResponse;
import br.com.marcello.SpringGraphQL.domain.entities.UserAccount;
import br.com.marcello.SpringGraphQL.domain.exceptions.AccountAlreadyExistsException;
import br.com.marcello.SpringGraphQL.domain.exceptions.AccountNotFoundException;
import br.com.marcello.SpringGraphQL.domain.repositories.UserAccountRepository;
import br.com.marcello.SpringGraphQL.domain.services.UserAccountService;
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
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final MongoTemplate mongoTemplate;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, MongoTemplate mongoTemplate) {
        this.userAccountRepository = userAccountRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UserAccountResponse save(UserAccountInput userAccountInput) {
        UserAccount userAccount = this.userAccountRepository.findUserAccountByUsername(userAccountInput.getUsername());
        this.verifyThatAccountDoesntExists(userAccount, userAccountInput.getUsername());

        userAccount = this.convertInputToEntity(userAccountInput);
        this.userAccountRepository.save(userAccount);
        return this.convertEntityToResponse(userAccount);
    }

    @Override
    public List<UserAccountResponse> findAll() {
        List<UserAccount> userAccounts = this.userAccountRepository.findAll();

        List<UserAccountResponse> userAccountResponses = new ArrayList<>();
        userAccounts.forEach(
                userAccount -> userAccountResponses.add(this.convertEntityToResponse(userAccount))
        );

        return userAccountResponses;
    }

    @Override
    public UserAccountResponse findByUsername(String username) {
        UserAccount userAccount = this.userAccountRepository.findUserAccountByUsername(username);
        this.verifyThatAccountExists(userAccount, username);

        return this.convertEntityToResponse(userAccount);
    }

    @Override
    public UserAccountResponse update(String username, UserAccountInput fieldsToUpdate) {
        UserAccount userAccount = this.userAccountRepository.findUserAccountByUsername(username);
        this.verifyThatAccountExists(userAccount, username);

        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        Update update = new Update();

        Map<String,Object> nonNullFields = this.removeNullValuesToUpdate(fieldsToUpdate);

        this.setUpdateValues(update, nonNullFields);

        mongoTemplate.update(UserAccount.class).matching(query).apply(update).first();

        return this.convertMapValuesToResponse(nonNullFields);
    }

    @Override
    public UserAccountResponse delete(String username) {
        UserAccount userAccount = this.userAccountRepository.findUserAccountByUsername(username);
        this.verifyThatAccountExists(userAccount, username);

        this.userAccountRepository.deleteUserAccountByUsername(username);

        return this.convertEntityToResponse(userAccount);
    }

    @Override
    public void deleteAll() {
        this.userAccountRepository.deleteAll();
    }

    private UserAccount convertInputToEntity(UserAccountInput userAccountInput) {
        return new UserAccount().toBuilder()
                .username(userAccountInput.getUsername())
                .email(userAccountInput.getEmail())
                .address(userAccountInput.getAddress())
                .build();
    }

    private UserAccountResponse convertEntityToResponse(UserAccount userAccount) {
        return new UserAccountResponse().toBuilder()
                .username(userAccount.getUsername())
                .email(userAccount.getEmail())
                .address(userAccount.getAddress())
                .build();
    }

    private UserAccountResponse convertMapValuesToResponse(Map<String, Object> fieldsMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(fieldsMap, UserAccountResponse.class);
    }

    private Map<String,Object> removeNullValuesToUpdate(UserAccountInput fieldsToUpdate) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> fieldsMap = objectMapper.convertValue(fieldsToUpdate, Map.class);
        fieldsMap.values().removeIf(Objects::isNull);

        if (fieldsMap.containsKey("address"))
            this.removeNullValuesFromAddressMap(fieldsMap);

        return fieldsMap;
    }

    private Map<String, Object> removeNullValuesFromAddressMap(Map<String, Object> fieldsMap) {
        Map<String,Object> addressMap = (Map<String, Object>) fieldsMap.get("address");
        addressMap.values().removeIf(Objects::isNull);
        fieldsMap.put("address", addressMap);

        return fieldsMap;
    }

    private Update setUpdateValues(Update update, Map<String, Object> nonNullFields) {
        for (Map.Entry<String, Object> entry : nonNullFields.entrySet()) {

            if( entry.getKey().equalsIgnoreCase("address")) {

                Map<String, Object> addressMap = (Map<String, Object>) entry.getValue();

                for (Map.Entry<String, Object> addressEntry : addressMap.entrySet()) {
                    update.set(this.buildAddressUpdateKey(entry.getKey(), addressEntry.getKey()),
                            addressEntry.getValue());
                }

                continue;
            }

            update.set(entry.getKey(), entry.getValue());
        }

        return update;
    }

    private String buildAddressUpdateKey(String entryKey, String addressKey) {
        return entryKey + "." + addressKey;
    }

    private void verifyThatAccountDoesntExists(UserAccount userAccount, String username) {
        if(userAccount != null)
            throw new AccountAlreadyExistsException(username);
    }

    private void verifyThatAccountExists(UserAccount userAccount, String username) {
        if(userAccount == null)
            throw new AccountNotFoundException(username);
    }

}
