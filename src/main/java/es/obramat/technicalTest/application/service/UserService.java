package es.obramat.technicalTest.application.service;

import es.obramat.technicalTest.domain.model.security.User;
import es.obramat.technicalTest.infrastructure.persistence.security.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = findOne(username);

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        User user = userOpt.get();

        String[] authorities = new String[] { "user" };

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword()).roles(authorities).build();
    }

    public Optional<User> findOne(String username) {
        return userRepository.findById(username);
    }

    public void createUser(String username, String password) {
        password = encodePassword(password);

        User user = User.builder().username(username).password(password).build();

        userRepository.save(user);
    }

    public String encodePassword(String newPassword) {
        if (StringUtils.isBlank(newPassword)) {
            return null;
        }

        return bCryptPasswordEncoder.encode(newPassword);
    }
}
