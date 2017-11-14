package org.magic7.core.domain;


import javax.persistence.Entity;
import javax.persistence.Table;

import org.magic7.core.service.ServiceStaticInfo;

@Entity
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_ROW_ITEM")
public class MagicRowItem extends MagicSuperRowItem {

}
 
