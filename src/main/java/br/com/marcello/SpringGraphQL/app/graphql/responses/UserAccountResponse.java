package br.com.marcello.SpringGraphQL.app.graphql.responses;

import br.com.marcello.SpringGraphQL.domain.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserAccountResponse {

    private String username;
    private String email;
    private Address address;

}
