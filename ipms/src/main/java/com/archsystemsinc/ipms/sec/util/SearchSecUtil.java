package com.archsystemsinc.ipms.sec.util;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.data.jpa.domain.Specification;

import com.archsystemsinc.ipms.common.ClientOperation;
import com.archsystemsinc.ipms.common.IEntity;
import com.archsystemsinc.ipms.util.SearchCommonUtil;

public final class SearchSecUtil{
	
	private SearchSecUtil(){
		throw new UnsupportedOperationException();
	}
	
	// util
	
	public static < T extends IEntity >Specification< T > resolveConstraint( final ImmutableTriple< String, ClientOperation, String > constraint, final Class< T > clazz ){
		final String constraintName = constraint.getLeft();
		final boolean negated = isConstraintNegated( constraint );
		
		if( constraintName.equals( SearchCommonUtil.NAME ) ){
			return QuerySpecifications.getByNameSpecification( clazz, constraint.getMiddle(), constraint.getRight(), negated );
		}
		if( constraintName.equals( SearchCommonUtil.ID ) ){
			return QuerySpecifications.getByIdSpecification( clazz, Long.parseLong( constraint.getRight() ), negated );
		}
		return null;
	}
	
	static boolean isConstraintNegated( final ImmutableTriple< String, ClientOperation, String > constraint ){
		return constraint.getMiddle().isNegated();
	}
	
}
