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
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_PARTICIPANT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MagicParticipant {
	public enum ParticipantType {
		PROVIDER("PROVIDER",0),
		ACCESSOR("ACCESSOR",1);
		private String name;
		private Integer code;
		private ParticipantType(String name,Integer code) {
			this.name = name;
			this.code = code;
		}
		public static ParticipantType getPersistenceType(Integer code) {
			ParticipantType[] types = values();
			for(ParticipantType type:types) {
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
	
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"Participant";
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	
	@Column(name = "NAME", length = 300)
	private String name;
	
	@Column(name = "DESCRIPTION", length = 2000)
	private String description;
	
	@Column(name = "PARTICIPANT_TYPE",length=3)
	private Integer participantType;
	
	@Column(name="TOKEN",length=64)
	private String token;
	
	@Column(name = "SEQ",length=3)
	private Integer seq;
	
	@Column(name="GROUP_ID",length=64)
	private String groupId;

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
	public Integer getParticipantType() {
		return participantType;
	}
	public void setParticipantType(Integer participantType) {
		this.participantType = participantType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
