package com.archsystemsinc.ipms.web.controller.discovery;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

import com.archsystemsinc.ipms.common.util.RESTURIUtil;
import com.google.common.net.HttpHeaders;

@Controller
final class DiscoverabilityController{
	
	// API
	
	@RequestMapping( value = "/",method = RequestMethod.GET )
	@ResponseStatus( value = HttpStatus.NO_CONTENT )
	public final void adminRoot( final HttpServletRequest request, final HttpServletResponse response ){
		final String rootUri = request.getRequestURL().toString();
		
		// TODO: add these in a more dynamic way, not hardcoded
		final URI uri = new UriTemplate( "{rootUri}{resource}" ).expand( rootUri, "user" );
		final String linkToEntity = RESTURIUtil.createLinkHeader( uri.toASCIIString(), RESTURIUtil.REL_COLLECTION );
		final String linkToTest = RESTURIUtil.createLinkHeader( "test", RESTURIUtil.REL_COLLECTION );
		
		response.addHeader( HttpHeaders.LINK, RESTURIUtil.gatherLinkHeaders( linkToEntity, linkToTest ) );
	}
	
}
