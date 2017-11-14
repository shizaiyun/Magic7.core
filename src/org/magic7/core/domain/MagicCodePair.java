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
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_CODE_PAIR")
public class MagicCodePair {
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"CodePair";
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	
	@Column(name = "CODE_LID_ID",length = 64)
	private String codeLidId;
	
	@Column(name = "PARAMETE_NAME",length = 300)
	private String parameteName;
	
	@Column(name = "PARAMETE_DESCRIPTION",length = 1000)
	private String description;
	
	@Column(name = "VALUE_TYPE")
	private Integer valueType;
	
	@Column(name = "SEQ")
	private Integer seq;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCodeLidId() {
		return codeLidId;
	}
	public void setCodeLidId(String codeLidId) {
		this.codeLidId = codeLidId;
	}
	public String getParameteName() {
		return parameteName;
	}
	public void setParameteName(String parameteName) {
		this.parameteName = parameteName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getValueType() {
		return valueType;
	}
	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}
