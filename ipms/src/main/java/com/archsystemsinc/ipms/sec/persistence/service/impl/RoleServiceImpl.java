package com.archsystemsinc.ipms.sec.persistence.service.impl;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.archsystemsinc.ipms.common.ClientOperation;
import com.archsystemsinc.ipms.persistence.service.AbstractService;
import com.archsystemsinc.ipms.sec.model.Role;
import com.archsystemsinc.ipms.sec.persistence.dao.IRoleJpaDAO;
import com.archsystemsinc.ipms.sec.persistence.service.IRoleService;
import com.archsystemsinc.ipms.sec.util.SearchSecUtil;
import com.google.common.collect.Lists;

@Service
@Transactional
public class RoleServiceImpl extends AbstractService< Role > implements IRoleService{

	@Autowired
	public IRoleJpaDAO dao;

	public RoleServiceImpl(){
		super( Role.class );
	}

	// API

	// search

	@Override
	public List< Role > search( final ImmutableTriple< String, ClientOperation, String >... constraints ){
		final Specification< Role > firstSpec = SearchSecUtil.resolveConstraint( constraints[0], Role.class );
		Specifications< Role > specifications = Specifications.where( firstSpec );
		for( int i = 1; i < constraints.length; i++ ){
			specifications = specifications.and( SearchSecUtil.resolveConstraint( constraints[i], Role.class ) );
		}
		if( firstSpec == null ){
			return Lists.newArrayList();
		}

		return getDao().findAll( specifications );
	}

	// get/find

	@Override
	public Role findByName( final String name ){
		return dao.findByName( name );
	}

	// create

	@Override
	public Role create( final Role entity ){
		/*
		 * final long id = IdUtil.randomPositiveLong(); entity.setId( id );
		 */

		/*
		 * final List< Privilege > associationsTemp = Lists.newArrayList( entity.getPrivileges() ); entity.getPrivileges().clear(); for( final Privilege privilege : associationsTemp ){ entity.getPrivileges().add(
		 * associationDao.findByName( privilege.getName() ) ); }
		 */

		return super.create( entity );
	}

	// Spring

	@Override
	protected final IRoleJpaDAO getDao(){
		return dao;
	}

	@Override
	public Specification< Role > resolveConstraint( final ImmutableTriple< String, ClientOperation, String > constraint ){
		return SearchSecUtil.resolveConstraint( constraint, Role.class );
	}

	@Override
	protected JpaSpecificationExecutor< Role > getSpecificationExecutor(){
		return dao;
	}
}
