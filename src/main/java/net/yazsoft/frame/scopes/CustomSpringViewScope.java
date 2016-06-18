package net.yazsoft.frame.scopes;

import org.omnifaces.util.Faces;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;

public class CustomSpringViewScope implements Scope {

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		
		/**
		 * FIXME low importance edge case:
		 * when the app first startup, if a secured viewscoped bean requested it gives exception after login and redirection.
		 * 
		 * INFO: CDI @ViewScoped manager unavailable
		 * java.lang.NoClassDefFoundError: javax/enterprise/context/spi/Contextual
		 */
		
		Map<String, Object> viewMap = new HashMap<String, Object>();
		try {
			if (FacesContext.getCurrentInstance()!=null) {
				viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (viewMap.containsKey(name)) {
			return viewMap.get(name);
		} else {
			Object object = objectFactory.getObject();
			viewMap.put(name, object);

			return object;
		}
	}

	@Override
	public Object remove(String name) {
		return FacesContext.getCurrentInstance().getViewRoot().getViewMap()
				.remove(name);
	}

	@Override
	public String getConversationId() {
		return null;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		// Not supported
	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}
}