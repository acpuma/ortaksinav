package net.yazsoft.frame.security;

import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Roles;
import net.yazsoft.ors.entities.Students;
import net.yazsoft.ors.entities.Users;
import net.yazsoft.ors.students.StudentsDao;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

@Named
@ViewScoped
@Transactional
public class StudentsSecuritySer implements UserDetailsService {
    Logger logger= Logger.getLogger(StudentsSecuritySer.class);

    //get user from the database, via Hibernate
    @Inject
    private StudentsDao userDao;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        Students student = userDao.findByUserName(username);
        if (student==null) {
            throw new UsernameNotFoundException("Kullanıcı Bulunmadı");
        }
        Users user=new Users();
        user.setTid(student.getTid());
        user.setUsername(student.getUsername());
        user.setPassword(student.getPassword());
        logger.info("USER : " + user);
        return new User(
                user.getUsername(),
                user.getPassword(),
                true,true,true,true,
                /*
                user.getActive(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                */
                getAuthorities(user)
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Users user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        //Set<Roles> userRoles = user.getRoles();
        Collection<Roles> userRoles=user.getRolesCollection();

        String roleName="STUDENT";
        logger.info("ROLE : " + roleName);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        authorities.add(authority);

        return authorities;
    }

    public StudentsDao getUserDao() {
        return userDao;
    }

    public void setUserDao(StudentsDao userDao) {
        this.userDao = userDao;
    }
}
