package net.yazsoft.frame.security;

import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Roles;
import net.yazsoft.frame.hibernate.BaseSer;
import org.springframework.stereotype.Service;

import javax.inject.Named;

@Named
@ViewScoped
public class RolesSer extends BaseSer<Roles>{

}
