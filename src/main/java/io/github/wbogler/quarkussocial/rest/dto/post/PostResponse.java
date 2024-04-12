package io.github.wbogler.quarkussocial.rest.dto.post;

import java.time.LocalDateTime;

public record PostResponse(
        String post,
        LocalDateTime dateTime
) {
}
