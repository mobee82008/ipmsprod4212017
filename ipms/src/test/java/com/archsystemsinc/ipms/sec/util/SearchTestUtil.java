package com.archsystemsinc.ipms.sec.util;

import org.apache.commons.lang3.tuple.Pair;

import com.archsystemsinc.ipms.common.ClientOperation;
import com.archsystemsinc.ipms.util.SearchCommonUtil;

public final class SearchTestUtil{
	private SearchTestUtil(){
		throw new UnsupportedOperationException();
	}
	
	//
	
	public static String constructQueryString( final Pair< Long, ClientOperation > idOp, final Pair< String, ClientOperation > nameOp ){
		final Long id = ( idOp == null ) ? null : idOp.getLeft();
		final boolean negatedId = ( idOp == null ) ? false : idOp.getRight().isNegated();
		final String name = ( nameOp == null ) ? null : nameOp.getLeft();
		final boolean negatedName = ( nameOp == null ) ? false : nameOp.getRight().isNegated();
		return constructQueryString( id, negatedId, name, negatedName );
	}
	
	static String constructQueryString( final Long id, final boolean negatedId, final String name, final boolean negatedName ){
		final StringBuffer queryString = new StringBuffer();
		String op = null;
		if( id != null ){
			op = ( negatedId ) ? SearchCommonUtil.NEGATION + SearchCommonUtil.OP : SearchCommonUtil.OP;
			queryString.append( SearchCommonUtil.ID + op + id );
		}
		if( name != null ){
			if( queryString.length() != 0 ){
				queryString.append( SearchCommonUtil.SEPARATOR );
			}
			op = ( negatedName ) ? SearchCommonUtil.NEGATION + SearchCommonUtil.OP : SearchCommonUtil.OP;
			queryString.append( SearchCommonUtil.NAME + op + name );
		}
		
		return queryString.toString();
	}
	
	public static String constructQueryString( final Long id, final String name ){
		return constructQueryString( id, false, name, false );
	}

}
