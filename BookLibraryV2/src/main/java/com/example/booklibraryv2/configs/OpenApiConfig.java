package com.example.booklibraryv2.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

  private final Environment environment;


  @Bean
  public OpenAPI defineOpenApi() {
    var server = new Server();
    server.setUrl(environment.getProperty("api.server.url"));
    server.setDescription("DEV");

    var myContact = new Contact();
    myContact.setName("Ilya Kurnyshov");
    myContact.setEmail("IMKurnyshov@get-lab.ru");

    return new OpenAPI()
        .info(new Info()
            .title("BookLibraryV2 API")
            .version("0.0.1")
            .description("My pet-project with basic CRUD functional")
            .contact(myContact))
        .servers(List.of(server));
  }
}
