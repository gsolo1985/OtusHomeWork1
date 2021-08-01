package ru.otus.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.repositoriy.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        var user = repository.findByLogin(name).orElseThrow(() -> new UsernameNotFoundException(name));
        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(AuthorityUtils.createAuthorityList("ROLE_" + user.getRole()))
                .accountExpired(false)
                .accountLocked(false)
                .build();
    }
}
