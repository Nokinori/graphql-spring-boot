package graphql.kickstart.spring.webflux.boot;

import static org.assertj.core.api.Assertions.assertThat;

import graphql.kickstart.tools.SchemaParserOptions.GenericWrapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MonoGenericWrapperAlreadyDefinedTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void monoWrapper() throws IOException {
    val result = webTestClient.post()
        .uri("/graphql")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{ \"query\": \"query { hello } \"}")
        .exchange()
        .returnResult(String.class);
    val response = result.getResponseBody().blockFirst();
    assertThat(response).isEqualTo("{\"data\":{\"hello\":\"Hello world\"}}");
  }

  @TestConfiguration
  static class MonoConfiguration {
    @Bean
    GenericWrapper genericWrapper() {
      return GenericWrapper.withTransformer(
          Mono.class,
          0,
          Mono::toFuture,
          t -> t
      );
    }
  }

}