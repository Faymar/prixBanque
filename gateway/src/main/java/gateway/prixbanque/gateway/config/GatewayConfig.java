package gateway.prixbanque.gateway.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("gestionCompte", r -> r.path("/accounts/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8083"))
                .route("facturation", r -> r.path("/factures/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8082"))
                .route("transaction", r -> r.path("/releves/**", "/transactions/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8084"))
                .build();
    }
}
