package io.github.wbogler.quarkussocial.rest.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRequest(
        @NotBlank @NotNull
        String text
) {
}
