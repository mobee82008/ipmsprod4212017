package com.archsystemsinc.ipms.sec.persistence.service.impl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;

import com.archsystemsinc.ipms.persistence.service.AbstractServiceUnitTest;
import com.archsystemsinc.ipms.sec.model.Principal;
import com.archsystemsinc.ipms.sec.persistence.dao.IPrincipalJpaDAO;
import com.archsystemsinc.ipms.sec.persistence.service.impl.PrincipalServiceImpl;
import com.archsystemsinc.ipms.sec.persistence.util.FixtureFactory;
import com.google.common.collect.Lists;

public class PrincipalServiceUnitTest extends AbstractServiceUnitTest< Principal >{
	
	PrincipalServiceImpl instance;
	
	private IPrincipalJpaDAO daoMock;
	
	// fixtures
	
	@Override
	@Before
	public final void before(){
		instance = new PrincipalServiceImpl();
		
		daoMock = mock( IPrincipalJpaDAO.class );
		when( daoMock.save( any( Principal.class ) ) ).thenReturn( new Principal() );
		when( daoMock.findAll() ).thenReturn( Lists.<Principal> newArrayList() );
		instance.dao = daoMock;
		super.before();
	}
	
	// get
	
	@Test
	public final void whenGetIsTriggered_thenNoException(){
		configureGet( 1l );
		
		// When
		instance.findOne( 1l );
		
		// Then
	}
	
	@Test
	public final void whenGetIsTriggered_thenEntityIsRetrieved(){
		configureGet( 1l );
		// When
		instance.findOne( 1l );
		
		// Then
		verify( daoMock ).findOne( 1l );
	}
	
	// mocking behavior
	
	final Principal configureGet( final long id ){
		final Principal entity = new Principal();
		entity.setId( id );
		when( daoMock.findOne( id ) ).thenReturn( entity );
		return entity;
	}
	
	// template method
	
	@Override
	protected final PrincipalServiceImpl getAPI(){
		return instance;
	}
	
	@Override
	protected final JpaRepository< Principal, Long > getDAO(){
		return daoMock;
	}
	
	@Override
	protected final Principal createNewEntity(){
		return FixtureFactory.createNewPrincipal();
	}
	
	@Override
	protected ApplicationEventPublisher getEventPublisher(){
		return eventPublisher;
	}
	
	@Override
	protected void changeEntity( final Principal entity ){
		entity.setPassword( randomAlphabetic( 8 ) );
	}
	
}
