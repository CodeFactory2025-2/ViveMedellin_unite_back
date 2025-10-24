package com.communityapp.auth.service;
import com.communityapp.user.model.User;
import com.communityapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
/*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

*/
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println(" Buscando usuario: " + username);
    return userRepository.findByUsername(username)
            .map(user -> {
                System.out.println("Usuario encontrado: " + user.getUsername());
                System.out.println("Password hash en BD: " + user.getPassword());
                return user;
            })
            .orElseThrow(() -> {
                System.out.println("Usuario no encontrado en BD: " + username);
                return new UsernameNotFoundException("Usuario no encontrado: " + username);
            });
}


}

