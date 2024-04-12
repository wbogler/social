package io.github.wbogler.quarkussocial.repository.user;

import io.github.wbogler.quarkussocial.domain.domain.FollowerModel;
import io.github.wbogler.quarkussocial.domain.domain.UserModel;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.h2.engine.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<FollowerModel> {

    public boolean followers(UserModel follower, UserModel user){
//        Map<String, Object> params = new HashMap<>();
//        params.put("follower", follower);
//        params.put("user", user);
        var params = Parameters.with("follower", follower).and("user", user).map();
        PanacheQuery<FollowerModel> query =  find("follower = :follower and user = :user ", params);
        Optional<FollowerModel> result = query.firstResultOptional();
        return result.isPresent();
    }

    public List<FollowerModel> findByUser(Long userId){
        return find("user.id", userId).list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long id) {
        var params = Parameters.with("userId", id).and("followerId",followerId).map();
        delete("follower.id = :followerId and user.id = :userId",params);
    }
}
