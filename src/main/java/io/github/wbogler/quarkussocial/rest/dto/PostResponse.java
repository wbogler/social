package io.github.wbogler.quarkussocial.rest.dto;

import java.time.LocalDateTime;

public record PostResponse(
        String post,
        LocalDateTime dateTime
) {
}
