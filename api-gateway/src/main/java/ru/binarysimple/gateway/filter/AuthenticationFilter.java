package ru.binarysimple.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

//    @Value("${jwt.secret}")
//    private String jwtSecret;

    private final SecretKey secretKey;
    private final int jwtExpiration;

    public AuthenticationFilter(@Value("${jwt.secret}") String secret,
                                @Value("${jwt.expiration}") int jwtExpiration) {
        super(Config.class);

        String trimmedSecret = secret.trim();
        log.info("JWT Secret (first 10 chars): {}...", trimmedSecret.substring(0, Math.min(10, trimmedSecret.length())));

        byte[] decodedKey = Decoders.BASE64.decode(trimmedSecret);
        log.info("Decoded JWT secret length: {} bytes", decodedKey.length); // Должно быть 32
        log.info(Arrays.toString(decodedKey));

        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtExpiration = jwtExpiration;
    }

    /***
     * Ключевое отличие между GET и POST
     * GET-запросы к /actuator/health могут кэшироваться или обрабатываться особым образом
     * POST-запросы с телом (как /auth/register) требуют полной маршрутизации и чтения тела
     * Если сервис недоступен, а тело уже прочитано, может возвращаться 405 вместо 503
     * @param config
     * @return
     */

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            String method = request.getMethod().name();
            String requestId = exchange.getLogPrefix();

            log.debug("[{}] Request received: {} {}, headers: {}", requestId, method, path, request.getHeaders());

            // Пропускаем публичные эндпоинты
            if (isPublicEndpoint(path)) {
                log.debug("[{}] Request to public endpoint {}, passing through", requestId, path);
                return chain.filter(exchange);
            }

            // Проверяем заголовок Authorization
            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.debug("[{}] Missing or invalid Authorization header: {}", exchange.getLogPrefix(), authHeader);
                return onError(exchange, "Missing or invalid Authorization header",
                        HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                // Валидируем токен
                if (!validateToken(token)) {
                    log.debug("[{}] Invalid token provided", exchange.getLogPrefix());
                    return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                }

                // Извлекаем claims и добавляем в заголовки
                Claims claims = getClaims(token);
                String username = claims.getSubject();
//                String userId = claims.get("id", String.class);

                // Добавляем информацию о пользователе в заголовки
                ServerHttpRequest modifiedRequest = request.mutate()
//                        .header("X-User-Id", userId)
                        .header("X-Username", username)
                        .header("X-Roles", claims.get("roles", List.class).toString())
                        .header("X-Request-Id", requestId + "-auth")
                        .build();

                log.debug("[{}] Token validated successfully for user: {}, roles: {}", exchange.getLogPrefix(), username, claims.get("roles", List.class));
                log.debug("[{}] Adding headers to request: X-Username={}, X-Roles={}", exchange.getLogPrefix(), username, claims.get("roles", List.class));
                log.debug("[{}] Routing request to downstream service", exchange.getLogPrefix());

                return chain.filter(exchange.mutate().request(modifiedRequest).build());

            } catch (Exception e) {
                log.error("[{}] Error validating token: {}", exchange.getLogPrefix(), e.getMessage(), e);
                return onError(exchange, "Token validation failed", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/auth/") ||
                path.startsWith("/actuator/") ||
                path.equals("/health") ||
                path.equals("/info");
    }

    private boolean validateToken(String token) {
/**
 * затычка, чтобы не возиться с ключами.
 * todo вместо одинакового секрета использовать открытый ключ openssl и k8s secrets
 */
        try {
            Claims claims = getClaims(token);
            String username = claims.getSubject();
            return username != null;
        } catch (Exception e) {
            log.warn("Failed to validate token via AuthClient: {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
//                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("JWT parsing error: {}", e.getMessage());
            throw e;
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // Конфигурация фильтра
    }
}