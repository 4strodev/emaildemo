package com.astrodev.features.users;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
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
    @Inject
    Mailer mailer;

    @Transactional
    public void create(UUID id, CreateUserDTO createUserDTO) {
        LOG.info("Creating user");
        var userEntity = new UserEntity();
        userEntity.id = id;
        userEntity.email = createUserDTO.email();
        userEntity.password = BCrypt.hashpw(createUserDTO.password(), BCrypt.gensalt(12));
        entityManager.persist(userEntity);
    }

    public void sendEmail(String to, String message) {
        mailer.send(Mail.withHtml(
                to,
                "Welcome Email",
                message
        ));
    }
}
