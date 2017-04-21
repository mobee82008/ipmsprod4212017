package com.archsystemsinc.ipms.web.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.archsystemsinc.ipms.client.template.IRESTTemplate;
import com.archsystemsinc.ipms.common.IEntity;
import com.archsystemsinc.ipms.test.AbstractRESTIntegrationTest;
import com.archsystemsinc.ipms.testing.security.AuthenticationUtil;
import com.google.common.collect.Ordering;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public abstract class AbstractSortRESTIntegrationTest< T extends IEntity > extends AbstractRESTIntegrationTest{
	
	// tests
	
	// GET (paged)
	
	@Test
	public final void whenResourcesAreRetrievedSorted_thenNoExceptions(){
		givenAuthenticated().get( getURI() + "?page=0&size=41&sortBy=name" );
	}
	
	@Test
	public final void whenResourcesAreRetrievedSorted_then200IsReceived(){
		final Response response = givenAuthenticated().get( getURI() + "?page=0&size=1&sortBy=name" );
		
		assertThat( response.getStatusCode(), is( 200 ) );
	}
	
	@SuppressWarnings( "unchecked" )
	@Test
	@Ignore( "http://forum.springsource.org/showthread.php?124083-Sort-case-sensitivey-in-Spring-Data-%28JPA%29&p=404901#post404901" )
	public final void whenResourcesAreRetrievedSorted_thenResourcesAreIndeedOrdered(){
		getAPI().createAsResponse( getAPI().createNewEntity() );
		getAPI().createAsResponse( getAPI().createNewEntity() );
		
		// When
		final Response response = givenAuthenticated().get( getURI() + "?page=0&size=4&sortBy=name" );
		final List< T > resourcesPagedAndSorted = getAPI().getMarshaller().decode( response.asString(), List.class );
		
		// Then
		assertTrue( getOrdering().isOrdered( resourcesPagedAndSorted ) );
	}
	
	@SuppressWarnings( "unchecked" )
	@Test
	public final void whenResourcesAreRetrievedNotSorted_thenResourcesAreNotOrdered(){
		getAPI().createAsResponse( getAPI().createNewEntity() );
		getAPI().createAsResponse( getAPI().createNewEntity() );
		
		// When
		final Response response = givenAuthenticated().get( getURI() + "?page=0&size=6" );
		final List< T > resourcesPagedAndSorted = getAPI().getMarshaller().decode( response.asString(), List.class );
		
		// Then
		assertFalse( getOrdering().isOrdered( resourcesPagedAndSorted ) );
	}
	
	@Test
	public final void whenResourcesAreRetrievedByInvalidSorting_then400IsReceived(){
		// When
		final Response response = givenAuthenticated().get( getURI() + "?page=0&size=4&sortBy=invalid" );
		
		// Then
		assertThat( response.getStatusCode(), is( 400 ) );
	}
	
	// util
	
	protected final RequestSpecification givenAuthenticated(){
		return AuthenticationUtil.givenBasicAuthenticated();
	}
	
	// template method
	
	protected abstract IRESTTemplate< T > getAPI();
	
	protected abstract Ordering< T > getOrdering();
	
	protected abstract String getURI();
	
	protected abstract T createNewEntity();
	
}
