package securitytut.bloggy.services;

import securitytut.bloggy.domain.entities.User;

import java.util.UUID;

public interface UserService {

    User getUserById(UUID id);
}
