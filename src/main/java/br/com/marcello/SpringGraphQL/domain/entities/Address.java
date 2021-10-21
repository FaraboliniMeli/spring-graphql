package br.com.marcello.SpringGraphQL.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Address {

    private String country;
    private String state;
    private String city;
    private String district;
    private String street;
    private String zipCode;

}
