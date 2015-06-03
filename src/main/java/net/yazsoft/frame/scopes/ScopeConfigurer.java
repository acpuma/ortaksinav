package net.yazsoft.frame.scopes;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ScopeConfigurer {

	@Bean
	public CustomScopeConfigurer customScope() {
		
		CustomScopeConfigurer configurer = new CustomScopeConfigurer();
		Map<String, Object> workflowScope = new HashMap<String, Object>();
		workflowScope.put("view", new CustomSpringViewScope());
		configurer.setScopes(workflowScope);

		return configurer;
	}
}
