package io.github.wbogler.quarkussocial.rest.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(

        @NotBlank
        String name,
        @NotNull
        Integer age
) {

}
