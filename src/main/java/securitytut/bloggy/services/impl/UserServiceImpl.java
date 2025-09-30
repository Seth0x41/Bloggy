package securitytut.bloggy.services.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import org.springframework.stereotype.Service;
import securitytut.bloggy.domain.entities.User;
import securitytut.bloggy.repositories.UserRepository;
import securitytut.bloggy.services.UserService;

import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID id) {
    return userRepository
            .findById(id)
            .orElseThrow(()-> new EntityNotFoundException("User not found with id"+id));
    }
}
