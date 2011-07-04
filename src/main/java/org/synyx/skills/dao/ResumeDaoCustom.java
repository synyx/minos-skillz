package org.synyx.skills.dao;

import org.synyx.hades.domain.Pageable;

import org.synyx.skills.domain.Resume;
import org.synyx.skills.domain.resume.ResumeFilter;
import org.synyx.skills.domain.resume.ResumeFilterSupport;

import java.util.List;
import java.util.Map;


/**
 * Custom DAO interface for {@link Resume} instances.
 *
 * @author Markus Knittig - knittig@synyx.de
 */
public interface ResumeDaoCustom {

    /**
     * Returns a {@link List} of {@link Resume}s for the given {@link ResumeFilterSupport} and parameters.
     *
     * @param pageable
     * @param resumeFilter
     * @param parameters
     * @return
     */
    List<Resume> findByFilter(Pageable pageable, ResumeFilter resumeFilter, Map<String, Object> parameters);
}
