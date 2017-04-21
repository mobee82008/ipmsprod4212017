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
import com.archsystemsinc.ipms.sec.model.Role;
import com.archsystemsinc.ipms.sec.persistence.dao.IRoleJpaDAO;
import com.archsystemsinc.ipms.sec.persistence.service.impl.RoleServiceImpl;
import com.archsystemsinc.ipms.sec.persistence.util.FixtureFactory;
import com.google.common.collect.Lists;

public class RoleServiceUnitTest extends AbstractServiceUnitTest< Role >{
	
	private RoleServiceImpl instance;
	
	private IRoleJpaDAO daoMock;
	
	// fixtures
	
	@Override
	@Before
	public final void before(){
		instance = new RoleServiceImpl();
		
		daoMock = mock( IRoleJpaDAO.class );
		when( daoMock.save( any( Role.class ) ) ).thenReturn( new Role() );
		when( daoMock.findAll() ).thenReturn( Lists.<Role> newArrayList() );
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
	
	final Role configureGet( final long id ){
		final Role entity = createNewEntity();
		entity.setId( id );
		when( daoMock.findOne( id ) ).thenReturn( entity );
		return entity;
	}
	
	// template method
	
	@Override
	protected final RoleServiceImpl getAPI(){
		return instance;
	}
	
	@Override
	protected final JpaRepository< Role, Long > getDAO(){
		return daoMock;
	}
	
	@Override
	protected ApplicationEventPublisher getEventPublisher(){
		return eventPublisher;
	}
	
	@Override
	protected final Role createNewEntity(){
		return FixtureFactory.createNewRole();
	}
	
	@Override
	protected void changeEntity( final Role entity ){
		entity.setName( randomAlphabetic( 6 ) );
	}
	
}
