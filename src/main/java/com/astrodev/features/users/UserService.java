package com.astrodev.features.users;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

@ApplicationScoped
public class UserService {
    @Inject
    Session session;

    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Transactional
    public void create(UUID id, CreateUserDTO createUserDTO) {
        LOG.info("Creating user");
        var userEntity = new UserEntity();
        userEntity.id = id;
        userEntity.email = createUserDTO.email();
        userEntity.password = BCrypt.hashpw(createUserDTO.password(), BCrypt.gensalt(12));
        session.persist(userEntity);
    }
}
