package org.magic7.core.domain;

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
import org.springframework.beans.BeanUtils;

@Entity
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_DIMENSION")
public class MagicDimension {
	
	public enum DefaultValue {
		CURRENT_USER("CURRENT_USER",0),
		CURRENT_DATE("CURRENT_DATE",1),
		USER_ROLE("USER_ROLE",2);
		private String name;
		private Integer code;
		private DefaultValue(String name,Integer code) {
			this.name = name;
			this.code = code;
		}
		public static DefaultValue getDefaultValue(Integer code) {
			DefaultValue[] types = values();
			for(DefaultValue type:types) {
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
	
	public enum Destination {
		FOR_DATA("FOR_DATA",0),
		FOR_QUERY("FOR_QUERY",1),
		FOR_BUTTON("FOR_BUTTON",2);
		private String name;
		private Integer code;
		private Destination(String name,Integer code) {
			this.name = name;
			this.code = code;
		}
		public static Destination getDestination(Integer code) {
			Destination[] types = values();
			for(Destination type:types) {
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
	
	public enum PageType {
		TEXT_EDITOR("TextEditor",0),
		DROP_DOWN_LIST("Drop_Down_List",1),
		POP_UP("Risk_Pop_Up",2),
		BUTTON("Button",3),
		TEXT_AREA("Text_Area",4),
		CHECK_BOX("CHECK_BOX",5),
		RADIO("RADIO",6);
		private String name;
		private Integer code;
		private PageType(String name,Integer code) {
			this.name = name;
			this.code = code;
		}
		public static PageType getQueryType(Integer code) {
			PageType[] types = values();
			for(PageType type:types) {
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
 
	public enum QueryType {
		PRECISE("Precise",0),
		VAGUE("Vague",1),
		BIGGER("Bigger",2),
		SMALLER("Smaller",3),
		IN("In",4);
		private String name;
		private Integer code;
		private QueryType(String name,Integer code) {
			this.name = name;
			this.code = code;
		}
		public static QueryType getQueryType(Integer code) {
			QueryType[] types = values();
			for(QueryType type:types) {
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
	
	public enum ValueType {
		STR_VALUE("STR_VALUE",0),
		NUM_VALUE("NUM_VALUE",1),
		DATE_VALUE("DATE_VALUE",2),
		BOOLEAN_VALUE("BOOLEAN_VALUE",3),
		ATTACHMENT_VALUE("ATTACHMENT_VALUE",4),
		LIST_STR_VALUE("LIST_STR_VALUE",5),
		TEMP("TEMP",6);
		private String name;
		private Integer code;
		private ValueType(String name,Integer code) {
			this.name = name;
			this.code = code;
		}
		public static ValueType getValueType(Integer code) {
			ValueType[] types = values();
			for(ValueType type:types) {
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
	
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"Dimension";
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	 
	@Column(name = "NAME", length = 128)
	private String name;
	 
	@Column(name = "DESCRIPTION", length = 2000)
	private String description;
	 
	@Column(name = "SPACE_ID", length = 64)
	private String spaceId;
	 
	@Column(name = "SPACE_REGION_ID", length = 64)
	private String spaceRegionId;
	
	@Column(name = "SPACE_REGION_NAME", length = 500)
	private String spaceRegionName;
	
	@Column(name = "SEQ",length = 12)
	private Integer seq;
	
	@Column(name = "VALUE_TYPE",length = 3)
	private Integer valueType;
	 
	@Column(name = "REQUIRED")
	private Boolean required;
	 
	@Column(name = "RELATION_ENTITY_NAME", length = 64)
	private String relationEntityName;
	
	@Column(name = "DUAL_ENTITY_NAME", length = 64)
	private String dualEntityName;
	
	@Column(name = "DUAL_PROPERTY_NAME", length = 64)
	private String dualPropertyName;
	 
	@Column(name = "PAGE_TYPE",length = 3)
	private Integer pageType;//说明页面显示类型：1输入框、2下拉框、3弹出框等
	
	@Column(name = "COLOR",length = 15)
	private String color;
	
	@Column(name = "CHOICE_CODE",length = 64)
	private String choiceCode;
	
	@Column(name = "PAGE_ACTION",length = 64)
	private String pageAction;
	
	@Column(name = "LOGIC_ACTION",length = 64)
	private String logicAction;
	
	@Column(name = "DIMENSION_VIRTUAL")
	private Boolean virtual;
	
	@Column(name = "LNK")
	private Boolean lnk;
	
	@Column(name = "DUAL_LNK")
	private Boolean dualLnk;
	
	@Column(name = "DESTINATION")
	private Integer destination = 0;
	
	@Column(name = "DISPLAY_NAME",length = 200)
	private String displayName;
	
	@Transient
	private Object queryCondition;
	@Column(name = "QUERY_TYPE",length = 3)
	private Integer queryType;
	
	@Column(name = "SPACE_NAME",length = 300)
	private String spaceName;
	
	@Column(name = "URL",length = 500)
	private String url;
	
	@Column(name = "CHOICE_NAME",length = 300)
	private String choiceName;
	
	@Column(name = "VALUE_NAME",length = 500)
	private String valueName;
	
	@Column(name = "DEFAULT_QUERY",length = 500)
	private String defaultQuery;
	
	@Column(name = "VISIBLE")
	private Boolean visible;
	
	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;
	
	@Column(name = "EDITABLE")
	private Boolean editable;
	
	@Transient
	private Object defaultQueryCondition;
	
	@Column(name = "DELIMITER",length = 10)//defaultQuery如果是查部门及其子部门时会有用
	private String delimiter;
	
	@Column(name = "LIKE_MODIFIER",length = 10)//用于defaultQuery的like查询
	private String likeModifier;
	
	@Column(name = "TAIL_LABEL",length = 100)
	private String tailLabel;//page element前的说明标签
	
	@Column(name = "BUSINESS_TRIGGERS",length = 1000)
	private String businessTriggers;
	
	@Column(name = "BUTTON_TRIGGER",length = 300)
	private String buttonTrigger;//当destination为FOR_BUTTON时有效,用来定义button的触发事件
	
	@Transient
	private String pageShowName;
	
	public String getChoiceName() {
		return choiceName;
	}
	public void setChoiceName(String choiceName) {
		this.choiceName = choiceName;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
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
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public Integer getValueType() {
		return valueType;
	}
	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public String getRelationEntityName() {
		return relationEntityName;
	}
	public void setRelationEntityName(String relationEntityName) {
		this.relationEntityName = relationEntityName;
	}
	public Integer getPageType() {
		return pageType;
	}
	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}
	public Object getQueryCondition() {
		return queryCondition;
	}
	public void setQueryCondition(Object queryCondition) {
		this.queryCondition = queryCondition;
	}
	public Integer getQueryType() {
		return queryType;
	}
	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSeverityId() {
		return choiceCode;
	}
	public void setChoiceCode(String choiceCode) {
		this.choiceCode = choiceCode;
	}
	public String getPageAction() {
		return pageAction;
	}
	public void setPageAction(String pageAction) {
		this.pageAction = pageAction;
	}
	public String getLogicAction() {
		return logicAction;
	}
	public void setLogicAction(String logicAction) {
		this.logicAction = logicAction;
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
	public MagicDimension clone() {
		MagicDimension clone = new MagicDimension();
		BeanUtils.copyProperties(this, clone);
		return clone;
	}
	public Integer getDestination() {
		return destination;
	}
	public void setDestination(Integer destination) {
		this.destination = destination;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	public Boolean getDualLnk() {
		return dualLnk;
	}
	public void setDualLnk(Boolean dualLnk) {
		this.dualLnk = dualLnk;
	}
	public String getDualEntityName() {
		return dualEntityName;
	}
	public void setDualEntityName(String dualEntityName) {
		this.dualEntityName = dualEntityName;
	}
	public String getDualPropertyName() {
		return dualPropertyName;
	}
	public void setDualPropertyName(String dualPropertyName) {
		this.dualPropertyName = dualPropertyName;
	}
	public String getDefaultQuery() {
		return defaultQuery;
	}
	public void setDefaultQuery(String defaultQuery) {
		this.defaultQuery = defaultQuery;
	}
	public String getDelimiter() {
		if(delimiter==null)
			return "";
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String getPageShowName() {
		return pageShowName;
	}
	public void setPageShowName(String pageShowName) {
		this.pageShowName = pageShowName;
	}
	public Object getDefaultQueryCondition() {
		return defaultQueryCondition;
	}
	public void setDefaultQueryCondition(Object defaultQueryCondition) {
		this.defaultQueryCondition = defaultQueryCondition;
	}
	public String getLikeModifier() {
		if(likeModifier==null)
			return "";
		return likeModifier;
	}
	public void setLikeModifier(String likeModifier) {
		this.likeModifier = likeModifier;
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
	public String getSpaceRegionName() {
		return spaceRegionName;
	}
	public void setSpaceRegionName(String spaceRegionName) {
		this.spaceRegionName = spaceRegionName;
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
	public String getBusinessTriggers() {
		return businessTriggers;
	}
	public void setBusinessTriggers(String businessTriggers) {
		this.businessTriggers = businessTriggers;
	}
	public String getButtonTrigger() {
		return buttonTrigger;
	}
	public void setButtonTrigger(String buttonTrigger) {
		this.buttonTrigger = buttonTrigger;
	}
}
 
