package br.com.marcello.SpringGraphQL.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "social_medias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SocialMedia {

    @Field(value = "account_username")
    private String accountUsername;

    private String facebook;
    private String instagram;
    private String twitter;


}
