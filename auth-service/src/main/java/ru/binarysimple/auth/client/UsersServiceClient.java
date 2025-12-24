package ru.binarysimple.auth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.binarysimple.auth.dto.CreateUserExternalDto;
import ru.binarysimple.auth.model.User;

@Component
public class UsersServiceClient {

    private final RestClient restClient;

    public UsersServiceClient(@Value("${users.service.url:http://localhost:8081}") String baseUrl) {
//    public UsersServiceClient() {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public CreateUserExternalDto createUser(CreateUserExternalDto user) {
        return restClient.post()
                .uri("/user")
                .body(user)
                .retrieve()
                .body(CreateUserExternalDto.class);
    }
}