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
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_SPACE_REGION_VIEW_ITEM")
public class MagicSpaceRegionViewItem {
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"SpaceRegionViewItem";
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")

	@Column(name="ID",length=64)
	private String id;
	
	@Column(name = "NAME", length = 500)
	private String name;
	
	@Column(name = "SPACE_ID", length = 64)
	private String spaceId;
	
	@Column(name = "SPACE_NAME", length = 500)
	private String spaceName;
	
	@Column(name = "SPACE_REGION_ID", length = 64)
	private String spaceRegionId;
	
	@Column(name = "SPACE_REGION_NAME", length = 500)
	private String spaceRegionName;
	
	@Column(name = "VIEW_ID", length = 64)
	private String viewId;
	
	@Column(name = "VIEW_NAME", length = 500)
	private String viewName;
	
	@Column(name = "DIMENSION_ID", length = 64)
	private String dimensionId;
	
	@Column(name = "PAGE_ACTION")
	private Boolean pageAction;
	
	@Column(name = "URL", length = 300)
	private String url;
	
	@Column(name = "PAGE_TYPE",length = 3)
	private Integer pageType;//说明页面显示类型：1输入框、2下拉框、3弹出框等
	
	@Column(name = "EDIT_ABLE")
	private Boolean editable;
	
	@Column(name = "SEQ",length = 3)
	private Integer seq=0;
	
	@Column(name = "REQUIRED")
	private Boolean required;
	
	@Column(name = "LAYOUT", length = 100)
	private String layout;
	
	@Column(name = "VISIBLE")
	private Boolean visible;
	
	@Column(name = "BUSINESS_CODE_ID",length = 64)
	private String businessCodeId;
	
	@Column(name = "BUSINESS_TRIGGERS",length = 300)
	private String businessTriggers;

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
	public String getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	public String getSpaceRegionId() {
		return spaceRegionId;
	}
	public void setSpaceRegionId(String spaceRegionId) {
		this.spaceRegionId = spaceRegionId;
	}
	public String getSpaceRegionName() {
		return spaceRegionName;
	}
	public void setSpaceRegionName(String spaceRegionName) {
		this.spaceRegionName = spaceRegionName;
	}
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getDimensionId() {
		return dimensionId;
	}
	public void setDimensionId(String dimensionId) {
		this.dimensionId = dimensionId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getPageAction() {
		return pageAction;
	}
	public void setPageAction(Boolean pageAction) {
		this.pageAction = pageAction;
	}
	public Integer getPageType() {
		return pageType;
	}
	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editAble) {
		this.editable = editAble;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public String getBusinessCodeId() {
		return businessCodeId;
	}
	public void setBusinessCodeId(String businessCodeId) {
		this.businessCodeId = businessCodeId;
	}
	public String getBusinessTriggers() {
		return businessTriggers;
	}
	public void setBusinessTriggers(String businessTriggers) {
		this.businessTriggers = businessTriggers;
	}
}
