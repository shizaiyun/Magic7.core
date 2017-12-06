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
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_TRIGGER_ASSEMBLER")
public class MagicTriggerAssembler {
	
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"TriggerAssembler";
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	@Column(name = "TRIGGER_NAME", length = 128)
	private String triggerName;
	@Column(name="DIMENSION_ID",length=64)
	private String dimensionId;
	@Column(name="CODE_LIB_ID",length=64)
	private String codeLibId;
	@Column(name="SEQ")
	private Integer seq;
	@Column(name = "SPACE_NAME", length = 300)
	private String spaceName;
	@Column(name = "REGION_NAME", length = 300)
	private String regionName;
	@Column(name = "DISPLAY_NAME", length = 300)
	private String displayName;
	@Column(name = "CODE_NAME", length = 300)
	private String codeName;
	@Column(name = "SIGNATURE", length = 300)
	private String signature;
	@Column(name = "PARAMETER_NAMES", length = 500)
	private String parameterNames;
	
	@Column(name = "ASSEMBLER_PARAMETER", length = 500)
	private String assemblerParameter;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public String getDimensionId() {
		return dimensionId;
	}
	public void setDimensionId(String dimensionId) {
		this.dimensionId = dimensionId;
	}
	public String getCodeLibId() {
		return codeLibId;
	}
	public void setCodeLibId(String codeLibId) {
		this.codeLibId = codeLibId;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getParameterNames() {
		return parameterNames;
	}
	public void setParameterNames(String parameterNames) {
		this.parameterNames = parameterNames;
	}
	public String getAssemblerParameter() {
		return assemblerParameter;
	}
	public void setAssemblerParameter(String assemblerParameter) {
		this.assemblerParameter = assemblerParameter;
	}
}
