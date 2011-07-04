package org.synyx.skills.dao;

import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;

import org.synyx.skills.domain.Category;
import org.synyx.skills.domain.MatrixTemplate;
import org.synyx.skills.domain.Skill;
import org.synyx.skills.domain.SkillEntry;
import org.synyx.skills.domain.SkillMatrix;

import java.util.List;


/**
 * @author Oliver Gierke - gierke@synyx.de
 */
public interface SkillzMatrixDao extends GenericDao<SkillMatrix, Long> {

    @Query("select count(l) from Level l")
    int findByNumberOfLevels();


    /**
     * Returns all {@link SkillMatrix} instances that own {@link SkillEntry}s of the given {@link Category}.
     *
     * @param category
     */
    @Query("select distinct m from SkillMatrix m, SkillEntry e where e member of m.entries and e.skill.category = ?")
    List<SkillMatrix> findByCategory(Category category);


    /**
     * Returns all {@link SkillMatrix} instances that own {@link SkillEntry}s for the given {@link Skill}.
     *
     * @param skill
     * @return
     */
    @Query("select distinct m from SkillMatrix m, SkillEntry e where e member of m.entries and e.skill = ?")
    List<SkillMatrix> findBySkill(Skill skill);


    /**
     * Returns all {@link SkillMatrix} instances bound to the given template.
     *
     * @param template
     * @return
     */
    @Query("select m from SkillMatrix m where m.template = ?")
    List<SkillMatrix> findByTemplate(MatrixTemplate template);
}
