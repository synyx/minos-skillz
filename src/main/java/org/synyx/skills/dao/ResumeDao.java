package org.synyx.skills.dao;

import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;

import org.synyx.minos.core.domain.User;
import org.synyx.skills.domain.Project;
import org.synyx.skills.domain.Resume;

import java.util.List;


/**
 * DAO interface to manage {@link Resume} instances.
 *
 * @author Oliver Gierke - gierke@synyx.de
 */
public interface ResumeDao extends GenericDao<Resume, Long>, ResumeDaoCustom {

    /**
     * Returns the {@link Resume} for the given user.
     *
     * @param user
     * @return
     */
    Resume findBySubject(User user);


    /**
     * Returns a {@link Resume}s containing references to the given {@link Project}.
     *
     * @param project
     * @return
     */
    @Query("select r from Resume r, Activity a where a member of r.references and a.project = ?")
    List<Resume> findByProject(Project project);
}
