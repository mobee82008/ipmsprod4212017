package com.archsystemsinc.ipms.web.common;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.archsystemsinc.ipms.client.template.IRESTTemplate;
import com.archsystemsinc.ipms.common.IEntity;
import com.archsystemsinc.ipms.test.AbstractRESTIntegrationTest;
import com.archsystemsinc.ipms.testing.security.AuthenticationUtil;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public abstract class AbstractPaginationRESTIntegrationTest< T extends IEntity > extends AbstractRESTIntegrationTest{
	
	// tests
	
	// GET (paged)
	
	@Test
	public final void whenResourcesAreRetrievedPaged_thenNoExceptions(){
		givenAuthenticated().get( getURI() + "?page=1&size=1" );
	}
	
	@Test
	public final void whenResourcesAreRetrievedPaged_then200IsReceived(){
		// When
		final Response response = givenAuthenticated().get( getURI() + "?page=0&size=1" );
		
		// Then
		assertThat( response.getStatusCode(), is( 200 ) );
	}
	
	@Test
	public final void whenFirstPageOfResourcesAreRetrieved_thenResourcesPageIsReturned(){
		getAPI().createAsResponse( getAPI().createNewEntity() );
		
		// When
		final Response response = givenAuthenticated().get( getURI() + "?page=0&size=1" );
		
		// Then
		assertFalse( getAPI().getMarshaller().decode( response.asString(), List.class ).isEmpty() );
	}
	
	@Test
	public final void whenPageOfResourcesAreRetrievedOutOfBounds_then404IsReceived(){
		// When
		final Response response = givenAuthenticated().get( getURI() + "?page=" + randomNumeric( 5 ) + "&size=1" );
		
		// Then
		assertThat( response.getStatusCode(), is( 404 ) );
	}
	
	@Test
	public final void whenResourcesAreRetrievedWithNonNumericPage_then400IsReceived(){
		// When
		final Response response = givenAuthenticated().get( getURI() + "?page=" + randomAlphabetic( 5 ).toLowerCase() + "&size=1" );
		
		// Then
		assertThat( response.getStatusCode(), is( 400 ) );
	}
	
	@Test
	public final void whenResourcesAreRetrievedWithNonNumericPageSize_then400IsReceived(){
		// When
		final Response response = givenAuthenticated().get( getURI() + "?page=0" + "&size=" + randomAlphabetic( 5 ) );
		
		// Then
		assertThat( response.getStatusCode(), is( 400 ) );
	}
	
	// util
	
	protected final RequestSpecification givenAuthenticated(){
		return AuthenticationUtil.givenBasicAuthenticated();
	}
	
	// template method
	
	protected abstract String getURI();
	
	protected abstract T createNewEntity();
	
	protected abstract IRESTTemplate< T > getAPI();
	
}
