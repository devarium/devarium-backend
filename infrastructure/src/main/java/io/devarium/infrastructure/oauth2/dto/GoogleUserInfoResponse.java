package io.devarium.infrastructure.oauth2.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleUserInfoResponse(
    String id,
    String email,
    boolean verifiedEmail,
    String name,
    String givenName,
    String familyName,
    String picture,
    String locale
) {

}
