package com.archsystemsinc.ipms.sec.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.archsystemsinc.ipms.client.RESTPaths;

@Component
public final class ExamplePaths{
	
	@Autowired RESTPaths paths;
	
	// API
	
	public final String getRootUri(){
		return paths.getContext() + "/api/";
	}
	
	public final String getUserUri(){
		return getRootUri() + "user";
	}
	
	public final String getPrivilegeUri(){
		return getRootUri() + "privilege";
	}
	
	public final String getRoleUri(){
		return getRootUri() + "role";
	}
	
	public final String getAuthenticationUri(){
		return getRootUri() + "authentication";
	}
	
	public final String getLoginUri(){
		return paths.getContext() + "/j_spring_security_check";
	}
	
}
