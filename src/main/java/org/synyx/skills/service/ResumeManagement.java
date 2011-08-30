package org.synyx.skills.service;

import org.synyx.minos.core.domain.User;
import org.synyx.skills.domain.Activity;
import org.synyx.skills.domain.MatrixTemplate;
import org.synyx.skills.domain.Resume;
import org.synyx.skills.domain.SkillMatrix;
import org.synyx.skills.domain.resume.ResumeAttributeFilter;

import java.util.List;


/**
 * Interface for resume management services.
 *
 * @author Oliver Gierke - gierke@synyx.de
 */
public interface ResumeManagement {

    /**
     * Returns the {@link Resume} of the given {@link User}. Creates a new one
     * for the given {@link User} if none already exists.
     *
     * @return
     */
    Resume getResume(User user);


    /**
     * Returns the {@link Resume} for the given {@link User}.
     *
     * @param id
     * @return
     */
    Resume getResume(Long id);


    /**
     * Saves the given {@link Resume}.
     *
     * @param resume
     * @return
     */
    Resume save(Resume resume);


    /**
     * Saves the given {@link SkillMatrix}.
     *
     * @param matrix
     * @return
     */
    SkillMatrix save(SkillMatrix matrix);


    /**
     * Applies the given {@link MatrixTemplate} to the given {@link Resume}. Will update an already existing
     * {@link SkillMatrix} linked to the {@link Resume} according to the {@link MatrixTemplate} or create a completely
     * new {@link SkillMatrix} for a new {@link Resume}.
     *
     * @param resume
     * @param template
     * @return
     */
    Resume save(Resume resume, MatrixTemplate template);


    /**
     * Returns an {@link Activity} by its id.
     *
     * @param id
     * @return
     */
    Activity getReference(Long id);


    /**
     * Saves a given {@link Activity}.
     *
     * @param reference
     * @return
     */
    Activity save(Activity reference);


    /**
     * Deletes the given {@link Activity}.
     *
     * @param reference
     */
    void delete(Activity reference);


    /**
     * Deletes the given {@link Resume}.
     *
     * @param resume
     */
    void delete(Resume resume);


    /**
     * Returns all available {@link ResumeAttributeFilter}s.
     *
     * @return
     */
    List<ResumeAttributeFilter> getResumeAttributeFilters();


    /**
     * Returns a given {@link User}s {@link Resume} filtered by the given {@link ResumeAttributeFilter}s {@link List}.
     *
     * @param filters
     * @return
     */
    Resume getFilteredResume(User user, List<ResumeAttributeFilter> filters);


    /**
     * Switch the {@link MatrixTemplate} to use for the given {@link User}s skill matrix.
     * If switched to a template different from the current one, the current skill matrix is
     * discarded!
     *
     * @param user
     * @param template
     */
    void switchMatrixTemplate(User user, MatrixTemplate template);
}
