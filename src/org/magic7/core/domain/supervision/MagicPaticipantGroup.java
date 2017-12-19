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
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_PATICIPANT_GROUP")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MagicPaticipantGroup {
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"PaticipantGroup";
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	
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
