package org.magic7.core.domain;

import java.util.Date;

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
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_SPACE")
public class MagicSpace {
	
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"Space";
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	 
	@Column(name = "CREATE_DATE")
	private Date createDate;
	 
	@Column(name = "CREATOR_ID", length = 64)
	private String creatorId;
	
	@Column(name = "NAME", length = 300)
	private String name;
	 
	@Column(name = "VALID")
	private Boolean valid;
	
	@Column(name = "LOGIC_PARTITION", length = 300)
	private String partition;//基于space的数据逻辑分区,如果space region没有逻辑分区,则该分区生效
	
	@Column(name = "TAB_LAYOUT")
	private String tabLayout;
	
	@Column(name = "COMPANY_CODE")
	private String companyCode;
	
	public enum TabLayout {
		VERTICAL("Vertical"),
		HORIZONTAL("Horizontal");
		private String name;
		private TabLayout(String name) {
			this.name = name;
		}
		public static TabLayout getTabLayout(String name) {
			TabLayout[] types = values();
			for(TabLayout type:types) {
				if(type.name.equals(name))
					return type;
			}
			return null;
		}
		public String toString() {
			return "name:"+this.name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	@Column(name = "DESCRIPTION", length = 2000)
	private String description;
	 
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
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTabLayout() {
		return tabLayout;
	}
	public void setTabLayout(String tabLayout) {
		this.tabLayout = tabLayout;
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
 
