package br.com.marcello.SpringGraphQL.app.graphql.inputs;

import br.com.marcello.SpringGraphQL.domain.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserAccountInput {

    private String id;
    private String username;
    private String email;
    private Address address;

}
