package com.astrodev.features.users.application;

import com.astrodev.features.users.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class UserService {
    private static final Logger LOG = Logger.getLogger(UserService.class);
    @Inject
    EntityManager entityManager;

    @Transactional
    public void save(CreateUserDTO createUserDTO) {
        LOG.info("Creating user");
        var user = new User();
        user.id = createUserDTO.id();
        user.email = createUserDTO.email();
        user.password = BCrypt.hashpw(createUserDTO.password(), BCrypt.gensalt(12));
        entityManager.merge(user);
    }
}
