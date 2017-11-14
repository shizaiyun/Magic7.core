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
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_CHOICE_ITEM")
public class MagicChoiceItem {
	
	private static final String SEQ = ServiceStaticInfo.TABLE_PREFIX+"ChoiceItem";
	
	@Id
	@GenericGenerator(name = SEQ + "_GENERATOR", strategy = Magic7IdGenerator.STRATEGY_NAME, parameters = { @Parameter(name = "pkColumnValue", value = SEQ) })
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ + "_GENERATOR")
	@Column(name="ID",length=64)
	private String id;
	
	@Column(name = "CHOICE_CODE", length = 128)
	private String choiceCode;
	
	@Column(name = "CHOICE_NAME", length = 128)
	private String choiceName;
	
	@Column(name = "VALUE_CODE", length = 128)
	private String valueCode;
	
	@Column(name = "VALUE_NAME", length = 128)
	private String valueName;
	
	
	@Column(name = "COLOR", length = 20)
	private String color;
	
	@Column(name = "SEQ")
	private Integer seq;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChoiceCode() {
		return choiceCode;
	}
	public void setChoiceCode(String choiceCode) {
		this.choiceCode = choiceCode;
	}
	public String getValueCode() {
		return valueCode;
	}
	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
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
	public String getChoiceName() {
		return choiceName;
	}
	public void setChoiceName(String choiceName) {
		this.choiceName = choiceName;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	
}
