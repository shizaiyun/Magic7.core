package org.magic7.core.domain.supervision;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.magic7.core.dao.Magic7IdGenerator;
import org.magic7.core.service.ServiceStaticInfo;

@Entity
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_PAGE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MagicPage {
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"Page";
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	
	@Column(name = "SPACE_NAME", length = 500)
	private String spaceName;
	
	@Column(name = "REGION_NAME", length = 500)
	private String regionName;
	
	@Column(name = "SPACE_ID", length = 64)
	private String spaceId;
	
	@Column(name = "REGION_ID", length = 64)
	private String regionId;
	
	@Column(name = "NAME", length = 300)
	private String name;
	
	@Column(name = "DESCRIPTION", length = 2000)
	private String description;
	
	@Column(name = "DATA_VIEW_ID", length = 64)
	private String dataViewId;
	
	@Column(name = "QUERY_VIEW_ID", length = 64)
	private String queryViewId;
	
	@Column(name = "BUTTON_VIEW_ID", length = 64)
	private String buttonViewId;

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
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
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
	public String getDataViewId() {
		return dataViewId;
	}
	public void setDataViewId(String dataViewId) {
		this.dataViewId = dataViewId;
	}
	public String getQueryViewId() {
		return queryViewId;
	}
	public void setQueryViewId(String queryViewId) {
		this.queryViewId = queryViewId;
	}
	public String getButtonViewId() {
		return buttonViewId;
	}
	public void setButtonViewId(String buttonViewId) {
		this.buttonViewId = buttonViewId;
	}
}
