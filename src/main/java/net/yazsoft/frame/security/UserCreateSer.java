package net.yazsoft.frame.security;

import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Users;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class UserCreateSer {
    private static final Logger logger = Logger.getLogger(UserCreateSer.class);

    @Inject
    private BCryptPasswordEncoder encoder;

    private Users user;

    @Autowired
    private UsersSer usersSer;

    public void create() {
        user.setPassword(encoder.encode(user.getPassword()));
        usersSer.create(user);
        //ADD DEFAULT USER ROLE
        logger.info("PASSWORD : " + user.getPassword());

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Welcome", user.getUsername());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


    @PostConstruct
    private void init() {
        this.user = new Users();
        System.out.println("Empty user object created.");
    }


    public void clear() {
        this.user = new Users();
        System.out.println("Cleared.");
    }

    public BCryptPasswordEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }


}
