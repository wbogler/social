package io.github.wbogler.quarkussocial.repository.user;

import io.github.wbogler.quarkussocial.domain.domain.UserModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserModel> {

}
