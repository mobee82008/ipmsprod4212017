package com.archsystemsinc.ipms.client;

import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.archsystemsinc.ipms.sec.model.Privilege;
import com.archsystemsinc.ipms.sec.model.Role;
import com.archsystemsinc.ipms.sec.model.dto.User;
import com.archsystemsinc.ipms.security.BasicHttpComponentsClientHttpRequestFactory;
import com.archsystemsinc.ipms.security.DigestHttpComponentsClientHttpRequestFactory;

@Component
public class RestTemplateFactoryBean implements FactoryBean< RestTemplate >, InitializingBean{
	private RestTemplate restTemplate;

	@Value( "${sec.auth.basic}" ) boolean basicAuth;
	@Value( "${http.req.timeout}" ) int timeout;

	// API

	@Override
	public RestTemplate getObject(){
		return restTemplate;
	}

	@Override
	public Class< RestTemplate > getObjectType(){
		return RestTemplate.class;
	}

	@Override
	public boolean isSingleton(){
		return true;
	}

	@Override
	public void afterPropertiesSet(){
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		final HttpComponentsClientHttpRequestFactory requestFactory;
		if( basicAuth ){
			requestFactory = new BasicHttpComponentsClientHttpRequestFactory( httpClient ){
				{
					setReadTimeout( timeout );
				}
			};
		}
		else{
			requestFactory = new DigestHttpComponentsClientHttpRequestFactory( httpClient ){
				{
					setReadTimeout( timeout );
				}
			};
		}
		restTemplate = new RestTemplate( requestFactory );

		restTemplate.getMessageConverters().add( marshallingHttpMessageConverter() );
	}

	//

	final MarshallingHttpMessageConverter marshallingHttpMessageConverter(){
		final MarshallingHttpMessageConverter marshallingHttpMessageConverter = new MarshallingHttpMessageConverter();
		marshallingHttpMessageConverter.setMarshaller( xstreamMarshaller() );
		marshallingHttpMessageConverter.setUnmarshaller( xstreamMarshaller() );

		return marshallingHttpMessageConverter;
	}

	final XStreamMarshaller xstreamMarshaller(){
		final XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
		xStreamMarshaller.setAutodetectAnnotations( true );
		xStreamMarshaller.setAnnotatedClasses( new Class[ ] { User.class, Role.class, Privilege.class } );
		xStreamMarshaller.getXStream().addDefaultImplementation( java.sql.Timestamp.class, java.util.Date.class );

		return xStreamMarshaller;
	}

}
