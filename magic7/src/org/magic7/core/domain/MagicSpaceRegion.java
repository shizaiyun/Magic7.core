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
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_SPACE_REGION")
public class MagicSpaceRegion {
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"SpaceRegion";
	
	public enum RegionType {
		MAIN("Main",0),
		TAB("TAB",1),
		DETAIL("Detail",2);
		private String name;
		private Integer code;
		private RegionType(String name,Integer code) {
			this.name = name;
			this.code = code;
		}
		public static RegionType getRegionType(Integer code) {
			RegionType[] types = values();
			for(RegionType type:types) {
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
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	 
	@Column(name = "NAME", length = 300)
	private String name;
	 
	@Column(name = "SPACE_ID", length = 64)
	private String spaceId;
	
	@Column(name = "SPACE_NAME", length = 300)
	private String spaceName;
	
	@Column(name = "MULTIPLY")
	private Boolean multiply;
	 
	@Column(name = "PARENT_ID", length = 64)
	private String parentId;
	 
	@Column(name = "DESCENDANT", length = 1000)
	private String descendant;
	
	@Column(name = "LNK")
	private Boolean lnk;
	
	@Column(name = "SEQ")
	private Integer seq;

	@Column(name = "PAGE_ACTIONS", length = 500)
	private String pageActions;
	
	@Column(name = "LOGIC_ACTIONS", length = 500)
	private String logicActions;
	
	@Column(name = "DESCRIPTION", length = 1000)
	private String description;
	
	@Column(name = "SOURCE_NAME", length = 500)
	private String sourceName;
	
	@Column(name = "REGION_TYPE", length = 3)
	private Integer regionType;//说明页面显示类型：main, tab等
	
	@Column(name = "DIMENSION_NUM", length = 3)
	private Integer dimensionNum;
	
	@Column(name = "EXTRA_EDITOR")
	private Boolean extraEditor;
	
	@Column(name = "LOGIC_PARTITION", length = 300)
	private String partition;//基于space region的数据逻辑分区,以此逻辑分区为最高优先级
	
	@Column(name = "COMPANY_CODE")
	private String companyCode;

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
	public Boolean getLnk() {
		return lnk;
	}
	public void setLnk(Boolean lnk) {
		this.lnk = lnk;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getPageActions() {
		return pageActions;
	}
	public void setPageActions(String pageActions) {
		this.pageActions = pageActions;
	}
	public String getLogicActions() {
		return logicActions;
	}
	public void setLogicActions(String logicActions) {
		this.logicActions = logicActions;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
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
	public String getPartition() {
		return partition;
	}
	public void setPartition(String partition) {
		this.partition = partition;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
}
 
