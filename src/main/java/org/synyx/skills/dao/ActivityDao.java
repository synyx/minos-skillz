package org.synyx.skills.dao;

import org.synyx.hades.dao.GenericDao;

import org.synyx.skills.domain.Activity;
import org.synyx.skills.domain.Project;

import java.util.List;


/**
 * DAO interface to manage {@link Activity} instances.
 *
 * @author Oliver Gierke - gierke@synyx.de
 */
public interface ActivityDao extends GenericDao<Activity, Long> {

    /**
     * Returns all {@link Activity}s that reference the given project.
     *
     * @param project
     * @return
     */
    List<Activity> findByProject(Project project);
}
