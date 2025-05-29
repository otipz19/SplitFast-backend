package ua.edu.ukma.cyber.soul.splitfast.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "SplitFast Backend",
        version = "1.0",
        description = "SplitFast application backend"
    ),
    servers = @Server(url = "https://api.splitfast.baby")
)
@SecurityScheme(
        name = OpenAPIConfiguration.BEARER_AUTH,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        paramName = HttpHeaders.AUTHORIZATION
)
public class OpenAPIConfiguration {

    public static final String BEARER_AUTH = "bearerAuth";

    static {
        io.swagger.v3.core.jackson.ModelResolver.enumsAsRef = true;
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .packagesToScan("ua.edu.ukma.cyber.soul.splitfast.controllers")
                .addOperationCustomizer((operation, method) -> {
                    operation.addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
                    return operation;
                })
                .build();
    }

    @Bean
    public GroupedOpenApi openApiGroup() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/auth/**", "/register/**")
                .packagesToScan("ua.edu.ukma.cyber.soul.splitfast.controllers")
                .build();
    }
}
