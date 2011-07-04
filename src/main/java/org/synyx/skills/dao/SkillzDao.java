package org.synyx.skills.dao;

import org.synyx.hades.dao.GenericDao;

import org.synyx.skills.domain.Category;
import org.synyx.skills.domain.Skill;

import java.util.List;


/**
 * @author Oliver Gierke - gierke@synyx.de
 */
public interface SkillzDao extends GenericDao<Skill, Long> {

    List<Skill> findByCategory(Category category);
}
