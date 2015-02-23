package net.yazsoft.frame.security;

import net.yazsoft.frame.scopes.SessionScoped;
import net.yazsoft.ors.entities.Users;

import javax.inject.Named;

@Named
@SessionScoped
public class SessionInfo {
    Users user;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
