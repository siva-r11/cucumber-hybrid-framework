package com.framework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request/response model for the user endpoints.
 *
 * <p>Lombok removes boilerplate; Jackson annotations make (de)serialization forgiving of extra
 * fields and skip nulls on the way out so partial-update payloads stay clean.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String id;
    private String name;
    private String job;
    private String email;
    private String createdAt;
    private String updatedAt;
}
