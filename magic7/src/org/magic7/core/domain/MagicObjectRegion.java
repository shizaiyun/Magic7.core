package org.magic7.core.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.magic7.core.dao.Magic7IdGenerator;
import org.magic7.core.service.ServiceStaticInfo;

@Entity
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_OBJECT_REGION")
public class MagicObjectRegion {
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"ObjectRegion";
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	
	@Column(name = "OBJECT_ID", length = 64)
	private String objectId;
	
	@Column(name = "NAME", length = 64)
	private String name;
	
	@Column(name = "SPACE_ID", length = 64)
	private String spaceId;
	
	@Column(name = "SPACE_NAME", length = 200)
	private String spaceName;
	
	@Column(name = "MULTIPLY")
	private Boolean multiply;
	
	@Column(name = "PARENT_ID", length = 64)
	private String parentId;
	
	@Column(name = "DESCENDANT", length = 64)
	private String descendant;
	
	@Column(name = "SPACE_REGION_ID", length = 64)
	private String spaceRegionId;
	
	@Column(name = "SPACE_REGION_NAME", length = 300)
	private String spaceRegionName;
	
	@Column(name = "REGION_TYPE", length = 3)
	private Integer regionType;//说明页面显示类型：main, tab等
	
	@Column(name = "SEQ")
	private Integer seq;
	
	@Column(name = "HAS_DATA")
	private Boolean hasData = false;
	
	@Column(name = "SOURCE_NAME", length = 500)
	private String sourceName;
	
	@Column(name = "DIMENSION_NUM", length = 3)
	private Integer dimensionNum;
	
	@Column(name = "EXTRA_EDITOR")
	private Boolean extraEditor;
	
	@Transient
	private List<MagicRegionRow> rows;
	@Transient
	private String nameShow;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
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
	public Boolean getMultiply() {
		return multiply;
	}
	public void setMultiply(Boolean multiply) {
		this.multiply = multiply;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getDescendant() {
		return descendant;
	}
	public void setDescendant(String descendant) {
		this.descendant = descendant;
	}
	public String getSpaceRegionId() {
		return spaceRegionId;
	}
	public void setSpaceRegionId(String spaceRegionId) {
		this.spaceRegionId = spaceRegionId;
	}
	public List<MagicRegionRow> getRows() {
		return rows;
	}
	public void setRows(List<MagicRegionRow> rows) {
		this.rows = rows;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public Integer getRegionType() {
		return regionType;
	}
	public void setRegionType(Integer regionType) {
		this.regionType = regionType;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public Boolean getHasData() {
		return hasData;
	}
	public void setHasData(Boolean hasData) {
		this.hasData = hasData;
	}
	public String getNameShow() {
		return nameShow;
	}
	public void setNameShow(String nameShow) {
		this.nameShow = nameShow;
	}
	public Integer getDimensionNum() {
		return dimensionNum;
	}
	public void setDimensionNum(Integer dimensionNum) {
		this.dimensionNum = dimensionNum;
	}
	public Boolean getExtraEditor() {
		return extraEditor;
	}
	public void setExtraEditor(Boolean extraEditor) {
		this.extraEditor = extraEditor;
	}
	public String getSpaceRegionName() {
		return spaceRegionName;
	}
	public void setSpaceRegionName(String spaceRegionName) {
		this.spaceRegionName = spaceRegionName;
	}
}
 
