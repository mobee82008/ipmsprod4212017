package com.archsystemsinc.ipms.sec.model.dto;


import com.archsystemsinc.ipms.sec.model.Principal;
import com.google.common.base.Function;

public final class PrincipalToUserFunction implements Function< Principal, User >{
	
	@Override
	public final User apply( final Principal principal ){
		return new User( principal );
	}
	
}
