package net.yazsoft.frame.security;

import net.yazsoft.frame.hibernate.BaseSer;
import net.yazsoft.frame.entities.Users;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class UsersSer extends BaseSer<Users> {
    private Logger logger= Logger.getLogger(UsersSer.class);

    @Inject
    UsersDao usersDao;

    public Users findByUsername(final String username) {
        return usersDao.findByUserName(username);
    }

}
