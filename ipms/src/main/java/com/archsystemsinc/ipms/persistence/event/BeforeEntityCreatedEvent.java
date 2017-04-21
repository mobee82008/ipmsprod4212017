package com.archsystemsinc.ipms.persistence.event;

import org.springframework.context.ApplicationEvent;

import com.archsystemsinc.ipms.common.IEntity;
import com.google.common.base.Preconditions;

public final class BeforeEntityCreatedEvent< T extends IEntity > extends ApplicationEvent{
	private final Class< T > clazz;
	private final T entity;
	
	public BeforeEntityCreatedEvent( final Object sourceToSet, final Class< T > clazzToSet, final T entityToSet ){
		super( sourceToSet );
		
		Preconditions.checkNotNull( clazzToSet );
		clazz = clazzToSet;
		
		Preconditions.checkNotNull( entityToSet );
		entity = entityToSet;
	}
	
	// API
	
	public final Class< T > getClazz(){
		return clazz;
	}
	
	public final T getEntity(){
		return Preconditions.checkNotNull( entity );
	}
	
}
