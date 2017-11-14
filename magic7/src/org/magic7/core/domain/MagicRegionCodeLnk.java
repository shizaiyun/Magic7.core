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
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_REGION_CODE_LNK")
public class MagicRegionCodeLnk {
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"RegionCodeLnk";
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	
	@Column(name = "SPACE_NAME",length = 300)
	private String spaceName;
	
	@Column(name = "SPACE_ID", length = 64)
	private String spaceId;
	
	@Column(name = "REGION_NAME", length = 500)
	private String regionName;
	
	@Column(name = "REGION_ID",length = 64)
	private String regionId;
	
	@Column(name = "CODE_LID_ID",length = 64)
	private String codeLidId;
	
	@Column(name = "PARAMETER_NAMES", length = 500)
	private String parameterNames;
	
	@Column(name = "SIGNATURE", length = 300)
	private String signature;
	
	@Column(name = "CODE_NAME", length = 200)
	private String codeName;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	public String getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getCodeLidId() {
		return codeLidId;
	}
	public void setCodeLidId(String codeLidId) {
		this.codeLidId = codeLidId;
	}
	public String getParameterNames() {
		return parameterNames;
	}
	public void setParameterNames(String parameterNames) {
		this.parameterNames = parameterNames;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
}
