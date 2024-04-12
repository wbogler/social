package io.github.wbogler.quarkussocial.rest.dto.follow;

import java.util.List;

public record FollowerPerUserResponse (
        Integer followersCount,
        List<FollowerResponse> content
){
}
