package com.astrodev.features.users.application;

import com.astrodev.features.users.User;
import com.astrodev.shared.monads.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

@ApplicationScoped
public class UserService {
    private static final Logger LOG = Logger.getLogger(UserService.class);
    @Inject
    EntityManager entityManager;

    @Transactional
    public Result<Void, Throwable> save(CreateUserDTO createUserDTO) {
        try {
            LOG.info("Creating user");
            var user = new User();
            user.id = createUserDTO.id();
            user.email = createUserDTO.email();
            user.username = createUserDTO.username();
            user.password = BCrypt.hashpw(createUserDTO.password(), BCrypt.gensalt(12));
            entityManager.merge(user);
            entityManager.flush();
            return Result.ok(null);
        } catch (Throwable e) {
            return Result.err(e);
        }
    }

    public Result<User, Throwable> find(UUID id) {
        try {
            var user = entityManager.find(User.class, id);
            return Result.ok(user);
        } catch (Exception err) {
            return Result.err(err);
        }
    }
}
