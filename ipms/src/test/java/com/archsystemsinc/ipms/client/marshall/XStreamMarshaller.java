package com.archsystemsinc.ipms.client.marshall;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.archsystemsinc.ipms.sec.model.Privilege;
import com.archsystemsinc.ipms.sec.model.dto.User;
import com.google.common.base.Preconditions;
import com.thoughtworks.xstream.XStream;

@Component( "xstreamMarshaller" )
public final class XStreamMarshaller implements IMarshaller{
	
	private XStream xstream;
	
	public XStreamMarshaller(){
		super();
		
		xstream = new XStream();
		xstream.autodetectAnnotations( true );
		xstream.processAnnotations( User.class );
		xstream.processAnnotations( Privilege.class );
	}
	
	// API
	
	@Override
	public final < T >String encode( final T entity ){
		Preconditions.checkNotNull( entity );
		return xstream.toXML( entity );
	}
	
	@SuppressWarnings( "unchecked" )
	@Override
	public final < T >T decode( final String entityAsXML, final Class< T > clazz ){
		Preconditions.checkNotNull( entityAsXML );
		return (T) xstream.fromXML( entityAsXML );
	}
	
	@Override
	public final String getMime(){
		return MediaType.APPLICATION_XML.toString();
	}
	
}
