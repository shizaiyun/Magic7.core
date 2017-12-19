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
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_MENU")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MagicMenu {
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"Menu";
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	
	@Column(name = "MENU_MANAGER_ID", length = 64)
	private String menuManagerId;
	
	@Column(name = "PARENT_MENU_ID", length = 64)
	private String parentMenuId;
	
	@Column(name = "NAME", length = 300)
	private String name;
	
	@Column(name = "DESCRIPTION", length = 2000)
	private String description;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMenuManagerId() {
		return menuManagerId;
	}
	public void setMenuManagerId(String menuManagerId) {
		this.menuManagerId = menuManagerId;
	}
	public String getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
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
}
