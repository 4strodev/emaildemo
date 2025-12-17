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
    public void create(CreateUserDTO createUserDTO) {
        LOG.info("Creating user");
        var userEntity = new User();
        userEntity.id = createUserDTO.id();
        userEntity.email = createUserDTO.email();
        userEntity.password = BCrypt.hashpw(createUserDTO.password(), BCrypt.gensalt(12));
        entityManager.persist(userEntity);
    }
}
