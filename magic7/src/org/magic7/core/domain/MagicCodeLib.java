package org.magic7.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.magic7.core.dao.Magic7IdGenerator;
import org.magic7.core.service.ServiceStaticInfo;

@Entity
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_CODE_LIB")
public class MagicCodeLib {
	
	public enum CodeType {
		JAVA("JAVA",0),
		JS("JS",1);
		private String name;
		private Integer code;
		private CodeType(String name,Integer code) {
			this.name = name;
			this.code = code;
		}
		public static CodeType getCodeType(Integer code) {
			CodeType[] types = values();
			for(CodeType type:types) {
				if(type.code.intValue()==code.intValue())
					return type;
			}
			return null;
		}
		public String toString() {
			return "name:"+this.name+";"+"code:"+this.code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
	}
	
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"CodeLib";
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	
	@Column(name = "NAME", length = 300)
	private String name;
	
	@Column(name = "DESCRIPTION", length = 4000)
	private String description;
	
	@Column(name = "code", length = 4000)
	private String code;
	
	@Column(name = "SIGNATURE", length = 300)
	private String signature;
	
	@Column(name = "CODE_TYPE")
	private Integer codeType;
	
	@Column(name = "PACKAGES", length = 1000)
	private String packages = null;
	
	@Column(name = "PARAMETER_NAME")
	private String parameterNames = null;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Integer getCodeType() {
		return codeType;
	}
	public String getCodeTypeName() {
		CodeType type = CodeType.getCodeType(codeType);
		if(type==null)
			return null;
		return type.name;
	}
	public void setCodeType(Integer codeType) {
		this.codeType = codeType;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getParameterNames() {
		return parameterNames;
	}
	public void setParameterNames(String parameterNames) {
		this.parameterNames = parameterNames;
	}
}
