package br.com.marcello.SpringGraphQL.domain.exceptions;

public class NoneSocialMediasLinkedException extends RuntimeException {

    public NoneSocialMediasLinkedException(String username) {
        super("The account with username " + username + " hasn't any Social Medias linked.");
    }

}
