package org.magic7.core.dao;

import org.magic7.core.domain.supervision.MagicMenu;
import org.magic7.core.domain.supervision.MagicMenuConnector;
import org.magic7.core.domain.supervision.MagicPage;
import org.magic7.core.domain.supervision.MagicPageAssembler;
import org.magic7.core.domain.supervision.MagicParticipant;
import org.magic7.core.domain.supervision.MagicPaticipantGroup;

public class SupervisionDao extends BaseDao {
	private SupervisionDao(){}
	private static final SupervisionDao instance = new SupervisionDao();
	public static SupervisionDao getInstance() {
		return instance;
	}
	
	public Boolean savePage(MagicPage magicPage) {
		return super.save(magicPage);
	}
	public Boolean saveMenu(MagicMenu magicMenu) {
		return super.save(magicMenu);
	}
	public Boolean saveMenuConnector(MagicMenuConnector menuConnector) {
		return super.save(menuConnector);
	}
	public Boolean savePageAssembler(MagicPageAssembler pageAssembler) {
		return super.save(pageAssembler);
	}
	public Boolean saveParticipant(MagicParticipant participant) {
		return super.save(participant);
	}
	public Boolean savePaticipantGroup(MagicPaticipantGroup participantGroup) {
		return super.save(participantGroup);
	}
}
