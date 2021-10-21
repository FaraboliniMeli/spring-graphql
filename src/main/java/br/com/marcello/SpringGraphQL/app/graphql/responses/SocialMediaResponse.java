package br.com.marcello.SpringGraphQL.app.graphql.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SocialMediaResponse {

    private String accountUsername;
    private String facebook;
    private String instagram;
    private String twitter;

}
