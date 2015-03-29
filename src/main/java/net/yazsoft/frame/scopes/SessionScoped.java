package net.yazsoft.frame.scopes;

import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(WebApplicationContext.SCOPE_SESSION)
public @interface SessionScoped {

}
