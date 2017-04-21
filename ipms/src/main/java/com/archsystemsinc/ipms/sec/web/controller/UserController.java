package com.archsystemsinc.ipms.sec.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;

import com.archsystemsinc.ipms.common.exceptions.ConflictException;
import com.archsystemsinc.ipms.common.web.RestPreconditions;
import com.archsystemsinc.ipms.sec.model.dto.User;
import com.archsystemsinc.ipms.sec.persistence.service.dto.IUserService;
import com.archsystemsinc.ipms.sec.util.SecurityConstants;
import com.archsystemsinc.ipms.util.SearchCommonUtil;
import com.archsystemsinc.ipms.web.common.AbstractController;

@Controller
@RequestMapping( value = "user" )
public class UserController extends AbstractController< User >{

	@Autowired private IUserService service;

	public UserController(){
		super( User.class );
	}

	// API

	// search

	@RequestMapping( params = { SearchCommonUtil.Q_PARAM },method = RequestMethod.GET )
	@ResponseBody
	public List< User > search( @RequestParam( SearchCommonUtil.Q_PARAM ) final String queryString ){
		return searchInternal( queryString );
	}

	// find - all/paginated

	@RequestMapping( params = { "page", "size" },method = RequestMethod.GET )
	@ResponseBody
	public List< User > findPaginated( @RequestParam( "page" ) final int page, @RequestParam( "size" ) final int size, @RequestParam( value = "sortBy",required = false ) final String sortBy, final UriComponentsBuilder uriBuilder, final HttpServletResponse response ){
		return findPaginatedInternal( page, size, sortBy, uriBuilder, response );
	}

	@RequestMapping( method = RequestMethod.GET )
	@ResponseStatus( HttpStatus.SEE_OTHER )
	public void findAll( final UriComponentsBuilder uriBuilder, final HttpServletResponse response ){
		findAllRedirectToPagination( uriBuilder, response );
	}

	// find - one

	@RequestMapping( value = "/{id}",method = RequestMethod.GET )
	@ResponseBody
	public User findOne( @PathVariable( "id" ) final Long id, final UriComponentsBuilder uriBuilder, final HttpServletResponse response ){
		return findOneInternal( id, uriBuilder, response );
	}

	@RequestMapping( params = "name",method = RequestMethod.GET )
	@ResponseBody
	public User findOneByName( @RequestParam( "name" ) final String name ){
		User resource = null;
		try{
			resource = RestPreconditions.checkNotNull( getService().findByName( name ) );
		}
		catch( final InvalidDataAccessApiUsageException ex ){
			logger.error( "InvalidDataAccessApiUsageException on find operation" );
			logger.warn( "InvalidDataAccessApiUsageException on find operation", ex );
			throw new ConflictException( ex );
		}

		return resource;
	}

	// create
	@RequestMapping( method = RequestMethod.POST )
	@ResponseStatus( HttpStatus.CREATED )
	// @Secured( SecurityConstants.PRIVILEGE_USER_WRITE )
	public void create( @RequestBody final User resource, final UriComponentsBuilder uriBuilder, final HttpServletResponse response ){
		createInternal( resource, uriBuilder, response );
	}

	// update

	@RequestMapping( method = RequestMethod.PUT )
	@ResponseStatus( HttpStatus.OK )
	@Secured( SecurityConstants.CAN_USER_WRITE )
	public void update( @RequestBody final User resource ){
		updateInternal( resource );
	}

	@RequestMapping(value = "/getusers.html", produces = "application/json")
	@ResponseBody
	public List<User> getUsersForAutoComplete() {
		final List<User> list = findAllInternal();
		return list;

	}

	// delete

	@RequestMapping( value = "/{id}",method = RequestMethod.DELETE )
	@ResponseStatus( HttpStatus.NO_CONTENT )
	@Secured( SecurityConstants.CAN_USER_WRITE )
	public void delete( @PathVariable( "id" ) final Long id ){
		deleteByIdInternal( id );
	}

	// Spring

	@Override
	protected final IUserService getService(){
		return service;
	}

}
