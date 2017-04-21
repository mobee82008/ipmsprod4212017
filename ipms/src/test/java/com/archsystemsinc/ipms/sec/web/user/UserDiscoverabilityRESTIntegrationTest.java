package com.archsystemsinc.ipms.sec.web.user;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.springframework.beans.factory.annotation.Autowired;

import com.archsystemsinc.ipms.sec.client.template.UserRESTTemplateImpl;
import com.archsystemsinc.ipms.sec.model.dto.User;
import com.archsystemsinc.ipms.sec.test.SecDiscoverabilityRESTIntegrationTest;
import com.jayway.restassured.specification.RequestSpecification;

public class UserDiscoverabilityRESTIntegrationTest extends SecDiscoverabilityRESTIntegrationTest< User >{
	
	@Autowired private UserRESTTemplateImpl restTemplate;
	
	public UserDiscoverabilityRESTIntegrationTest(){
		super( User.class );
	}
	
	// tests
	
	// template method
	
	@Override
	protected final String getURI(){
		return getAPI().getURI();
	}
	
	@Override
	protected final void change( final User resource ){
		resource.setName( randomAlphabetic( 6 ) );
	}
	
	@Override
	protected final User createNewEntity(){
		return restTemplate.createNewEntity();
	}
	
	@Override
	protected final RequestSpecification givenAuthenticated(){
		return getAPI().givenAuthenticated();
	}
	
	@Override
	protected final UserRESTTemplateImpl getAPI(){
		return restTemplate;
	}
	
}
