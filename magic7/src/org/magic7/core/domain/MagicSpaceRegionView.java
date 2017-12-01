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
import org.magic7.core.domain.MagicDimension.Destination;
import org.magic7.core.service.ServiceStaticInfo;

@Entity
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_SPACE_REGION_VIEW")
public class MagicSpaceRegionView {
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"SpaceRegionView";
	
	public enum Layout {
		NORMAL("Normal",0),
		QUESTIONNAIRE("Questionnaire",1);
		private String name;
		private Integer code;
		private Layout(String name,Integer code) {
			this.name = name;
			this.code = code;
		}
		public static Layout getLayout(Integer code) {
			Layout[] types = values();
			for(Layout type:types) {
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
	
	public enum ViewType {
		DEFAULT("DEFAULT",0),
		CUSTOMER_DEFINE("CUSTOMER_DEFINE",1);
		private String name;
		private Integer code;
		private ViewType(String name,Integer code) {
			this.name = name;
			this.code = code;
		}
		public static ViewType getViewType(Integer code) {
			ViewType[] types = values();
			for(ViewType type:types) {
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
	
	@Column(name = "SPACE_ID", length = 64)
	private String spaceId;
	
	@Column(name = "SPACE_NAME", length = 500)
	private String spaceName;
	
	@Column(name = "SPACE_REGION_ID", length = 64)
	private String spaceRegionId;
	
	@Column(name = "SPACE_REGION_NAME", length = 500)
	private String spaceRegionName;
	
	@Column(name = "NAME", length = 500)
	private String name;
	
	@Column(name = "DISPLAY_NAME", length = 500)
	private String displayName;
	
	@Column(name = "LAYOUT", length = 100)
	private String layout;
	
	@Column(name = "DESTINATION")
	private Integer destination = MagicDimension.Destination.FOR_DATA.getCode();
	
	@Column(name = "VIEW_TYPE")
	private Integer viewType = ViewType.DEFAULT.code;
	
	@Column(name = "CUTOMER_PAGE_NAME",length=300)
	private String cutomerPageName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	public String getSpaceRegionId() {
		return spaceRegionId;
	}
	public void setSpaceRegionId(String spaceRegionId) {
		this.spaceRegionId = spaceRegionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	public String getSpaceRegionName() {
		return spaceRegionName;
	}
	public void setSpaceRegionName(String spaceRegionName) {
		this.spaceRegionName = spaceRegionName;
	}
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public Integer getDestination() {
		return destination;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDestinationName() {
		Destination d = MagicDimension.Destination.getDestination(destination);
		if(d==null)
			return null;
		return d.getName();
	}
	public void setDestination(Integer destination) {
		this.destination = destination;
	}
	public Integer getViewType() {
		return viewType;
	}
	public void setViewType(Integer viewType) {
		this.viewType = viewType;
	}
	public String getViewTypeName() {
		ViewType d = ViewType.getViewType(viewType);
		if(d==null)
			return null;
		return d.getName();
	}
	public String getCutomerPageName() {
		return cutomerPageName;
	}
	public void setCutomerPageName(String cutomerPageName) {
		this.cutomerPageName = cutomerPageName;
	}
}
