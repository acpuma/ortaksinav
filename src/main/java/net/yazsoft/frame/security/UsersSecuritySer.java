package net.yazsoft.frame.security;

import java.util.*;

import net.yazsoft.frame.entities.UserRoles;
import net.yazsoft.frame.entities.Users;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class UsersSecuritySer implements UserDetailsService {
    Logger logger= Logger.getLogger(UsersSecuritySer.class);

    //get user from the database, via Hibernate
    @Inject
    private UsersDao userDao;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        Users user = userDao.findByUserName(username);
        logger.info("USER : " + user);
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                getAuthorities(user)
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Users user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        //Set<Roles> userRoles = user.getRoles();
        Collection<UserRoles> userRoles=user.getUserRolesCollection();

        if(userRoles != null)
        {
            for (UserRoles role : userRoles) {
                String roleName=role.getRole().getName();
                logger.info("ROLE : " + roleName);
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
                authorities.add(authority);
            }
        }
        return authorities;
    }

    public UsersDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UsersDao userDao) {
        this.userDao = userDao;
    }
}
