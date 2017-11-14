package org.magic7.core.dao;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.magic7.utils.SecurityUtil;

public class Magic7IdGenerator extends TableGenerator{
	
	private Type oritype;
	private Boolean dev = true;
	
	public static final String STRATEGY_NAME = "org.magic7.core.dao.Magic7IdGenerator";
	
	public void configure(Type type, Properties params, Dialect dialect) throws MappingException {
		if(dev==true) {
			params.put(TABLE_PARAM, "TBL_SEQUENCE");
			params.put(VALUE_COLUMN_PARAM, "value");
			params.put(SEGMENT_COLUMN_PARAM, "type");
			params.put(SEGMENT_VALUE_PARAM, params.get("pkColumnValue"));
			oritype=type;
			super.configure(new LongType(), params, dialect);
		}
	}
	public synchronized Serializable generate(final SessionImplementor session, Object obj) {
		if(dev==false)
			return SecurityUtil.generateUUID();
		Serializable value=super.generate(session, obj);
		if(oritype.getReturnedClass()==String.class){
			return value.toString();
		}
		return value;
		
	}
}
