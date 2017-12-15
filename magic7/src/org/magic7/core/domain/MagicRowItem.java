package org.magic7.core.domain;


import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.magic7.core.service.ServiceStaticInfo;

@Entity
@Table(name = ServiceStaticInfo.TABLE_PREFIX+"_ROW_ITEM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MagicRowItem extends MagicSuperRowItem {

}
 
