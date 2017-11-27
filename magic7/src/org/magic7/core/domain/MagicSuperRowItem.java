package org.magic7.core.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.magic7.core.dao.Magic7IdGenerator;
import org.magic7.core.service.ServiceStaticInfo;

@Entity
@Table(name = "DEFAULT_ROW_ITEM")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)  
public class MagicSuperRowItem {
private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"RowItem";
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
 
	@Column(name = "NAME", length = 64)
	private String name;
	
	@Column(name = "SPACE_ID", length = 64)
	private String spaceId;
	
	@Column(name = "DESCRIPTION", length = 3000)
	private String description;
	
	@Column(name = "SPACE_REGION_ID", length = 64)
	private String spaceRegionId;
	
	@Column(name = "SEQ")
	private Integer seq;
	
	@Column(name = "REQUIRED")
	private Boolean required;
	
	@Column(name = "OBJECT_ID", length = 64)
	private String objectId;
	
	@Column(name = "REGION_ID", length = 64)
	private String regionId;
	
	@Column(name = "VALUE_TYPE")
	private Integer valueType;
	
	@Column(name = "STR_VALUE", length = 2000)
	private String strValue;
	
	@Column(name = "LIST_STR_VALUE", length = 960)
	private String listStrValue;
	
	@Column(name = "NUM_VALUE")
	private BigDecimal numValue;
	
	@Column(name = "DATE_VALUE", length = 64)
	private Date dateValue;
	
	@Column(name = "BOOLEAN_VALUE")
	private Boolean booleanValue;
	
	@Column(name = "ROW_ID", length = 64)
	private String rowId;
	
	@Column(name = "RELATION_ENTITY_NAME", length = 200)
	private String relationEntityName;
	
	@Column(name = "DUAL_ENTITY_NAME", length = 200)
	private String dualEntityName;
	
	@Column(name = "ENTITY_ID", length = 960)
	private String entityId;
	
	@Column(name = "CHOICE_CODE", length = 128)
	private String choiceCode;
	
	@Column(name = "CHOICE_NAME", length = 200)
	private String choiceName;
	
	@Column(name = "VALUE_NAME", length = 2000)
	private String valueName;
	
	@Column(name = "COLOR", length = 64)
	private String color;
	
	@Column(name = "PAGE_TYPE", length = 3)
	private Integer pageType;//说明页面显示类型：输入框=0、下拉框=1、弹出框=2等
	
	@Column(name = "DIMENSION_ID", length = 64)
	private String dimensionId;
	
	@Column(name = "DIMENSION_NAME", length = 500)
	private String dimensionName;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "DIMENSION_VIRTUAL")
	private Boolean virtual;
	
	@Column(name = "LNK")
	private Boolean lnk;
	
	@Column(name = "DUAL_LNK")
	private Boolean dualLnk;
	
	@Column(name = "DISPLAY_NAME",length = 200)
	private String displayName;
	
	@Column(name = "SPACE_NAME", length = 300)
	private String spaceName;
	
	@Column(name = "SPACE_REGION_NAME", length = 300)
	private String spaceRegionName;
	
	@Column(name = "REGION_NAME", length = 300)
	private String regionName;
	
	@Column(name = "DUAL_PROPERTY_NAME", length = 64)
	private String dualPropertyName;
	
	@Column(name = "TAIL_LABEL",length = 100)
	private String tailLabel;
	
	@Column(name = "VISIBLE")
	private Boolean visible;
	
	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;
	
	@Column(name = "EDITABLE")
	private Boolean editable;
	
	@Transient
	private MagicSpaceRegionViewItem viewItem;
	
	public String getDimensionId() {
		return dimensionId;
	}
	public void setDimensionId(String dimensionId) {
		this.dimensionId = dimensionId;
	}
	public String getDimensionName() {
		return dimensionName;
	}
	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSpaceRegionId() {
		return spaceRegionId;
	}
	public void setSpaceRegionId(String spaceRegionId) {
		this.spaceRegionId = spaceRegionId;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public Integer getValueType() {
		return valueType;
	}
	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}
	public String getStrValue() {
		return strValue;
	}
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	public BigDecimal getNumValue() {
		return numValue;
	}
	public void setNumValue(BigDecimal numValue) {
		this.numValue = numValue;
	}
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getRelationEntityName() {
		return relationEntityName;
	}
	public void setRelationEntityName(String relationEntityName) {
		this.relationEntityName = relationEntityName;
	}
	public String getChoiceCode() {
		return choiceCode;
	}
	public void setChoiceCode(String choiceCode) {
		this.choiceCode = choiceCode;
	}
	public String getChoiceName() {
		return choiceName;
	}
	public void setChoiceName(String choiceName) {
		this.choiceName = choiceName;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public Integer getPageType() {
		return pageType;
	}
	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Boolean getVirtual() {
		return virtual;
	}
	public void setVirtual(Boolean virtual) {
		this.virtual = virtual;
	}
	public Boolean getLnk() {
		return lnk;
	}
	public void setLnk(Boolean lnk) {
		this.lnk = lnk;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		if(StringUtils.isEmpty(entityId))
			return ;
		this.entityId = entityId.replaceAll("^[,]{1,}|[,]{1,}$", "");
	}
	public Boolean getBooleanValue() {
		return booleanValue;
	}
	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getListStrValue() {
		return listStrValue;
	}
	public void setListStrValue(String listStrValue) {
		this.listStrValue = listStrValue;
	}
	public MagicSpaceRegionViewItem getViewItem() {
		return viewItem;
	}
	public void setViewItem(MagicSpaceRegionViewItem viewItem) {
		this.viewItem = viewItem;
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
	public String getDualEntityName() {
		return dualEntityName;
	}
	public void setDualEntityName(String dualEntityName) {
		this.dualEntityName = dualEntityName;
	}
	public Boolean getDualLnk() {
		return dualLnk;
	}
	public void setDualLnk(Boolean dualLnk) {
		this.dualLnk = dualLnk;
	}
	public String getDualPropertyName() {
		return dualPropertyName;
	}
	public void setDualPropertyName(String dualPropertyName) {
		this.dualPropertyName = dualPropertyName;
	}
	public String getTailLabel() {
		return tailLabel;
	}
	public void setTailLabel(String tailLabel) {
		this.tailLabel = tailLabel;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	public String getSpaceRegionName() {
		return spaceRegionName;
	}
	public void setSpaceRegionName(String spaceRegionName) {
		this.spaceRegionName = spaceRegionName;
	}
}
 
