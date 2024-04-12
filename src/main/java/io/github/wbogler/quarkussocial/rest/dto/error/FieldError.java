package io.github.wbogler.quarkussocial.rest.dto.error;

public record FieldError(
        String field,
        String mensage
) {
}
