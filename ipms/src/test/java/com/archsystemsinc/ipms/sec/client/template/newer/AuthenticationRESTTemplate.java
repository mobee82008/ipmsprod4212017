package com.archsystemsinc.ipms.sec.client.template.newer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.archsystemsinc.ipms.client.marshall.IMarshaller;
import com.archsystemsinc.ipms.sec.client.ExamplePaths;
import com.archsystemsinc.ipms.sec.model.dto.User;
import com.archsystemsinc.ipms.testing.security.AuthenticationUtil;

@Component
public class AuthenticationRESTTemplate{
	
	@Autowired @Qualifier( "xstreamMarshaller" ) IMarshaller marshaller;
	@Autowired private RestTemplate restTemplate;
	
	@Autowired private ExamplePaths paths;
	
	//
	
	public final ResponseEntity< User > authenticate( final String username, final String password ){
		return restTemplate.exchange( getURI(), HttpMethod.POST, new HttpEntity< User >( createHeaders( username, password ) ), User.class );
	}
	
	// util
	
	final HttpHeaders createHeaders( final String username, final String password ){
		return new HttpHeaders(){
			{
				set( com.google.common.net.HttpHeaders.ACCEPT, marshaller.getMime() );
				
				final String basicAuthorizationHeader = AuthenticationUtil.createBasicAuthenticationAuthorizationHeader( username, password );
				set( com.google.common.net.HttpHeaders.AUTHORIZATION, basicAuthorizationHeader );
			}
		};
	}
	
	final String getURI(){
		return paths.getAuthenticationUri();
	}
	
}
