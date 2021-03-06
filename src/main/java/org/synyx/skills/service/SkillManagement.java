package org.synyx.skills.service;

import org.synyx.minos.core.domain.User;
import org.synyx.skills.domain.Category;
import org.synyx.skills.domain.Level;
import org.synyx.skills.domain.MatrixTemplate;
import org.synyx.skills.domain.Project;
import org.synyx.skills.domain.Responsibility;
import org.synyx.skills.domain.Skill;

import java.util.List;


/**
 * Interface for skill management services.
 *
 * @author Oliver Gierke - gierke@synyx.de
 */
public interface SkillManagement {

    /**
     * Returns all available {@link Level}s.
     *
     * @return
     */
    List<Level> getLevels();


    /**
     * Saves the given level.
     *
     * @param level
     * @return
     */
    Level save(Level level);


    /**
     * Deletes the given {@link Level}.
     *
     * @param level
     */
    void delete(Level level);


    /**
     * Returns the default {@link Level}.
     *
     * @return
     */
    Level getDefaultLevel();


    /**
     * Swaps the level ordinals with the next upper level.
     *
     * @param level
     */
    void moveLevelUp(Level level);


    /**
     * Swaps the level ordinals with the next lower level.
     *
     * @param level
     */
    void moveLevelDown(Level level);


    /**
     * Returns all projects.
     *
     * @return
     */
    List<Project> getProjects();


    /**
     * Returns all commonly available projects.
     *
     * @return
     */
    List<Project> getPublicProjects();


    /**
     * Returns all projects that are privately owned by the given {@link User}.
     *
     * @param user
     * @return
     */
    List<Project> getPrivateProjectsFor(User user);


    /**
     * Returns the project with the given id.
     *
     * @param id
     * @return
     */
    Project getProject(Long id);


    /**
     * Returns the project by the given name.
     *
     * @param name
     * @return
     */
    Project getProject(String name);


    /**
     * Saves the given {@link Project}.
     *
     * @param project
     * @return
     */
    Project save(Project project);


    /**
     * Deletes the given project.
     *
     * @param project
     */
    void delete(Project project);


    /**
     * Returns all {@link Category}.
     *
     * @return
     */
    List<Category> getCategories();


    /**
     * Returns the {@link Category} with the given id.
     *
     * @param id
     * @return
     */
    Category getCategory(Long id);


    /**
     * Saves the given {@link Skill}.
     *
     * @param skill
     */
    Skill save(Skill skill);


    /**
     * @param skill
     * @param category
     * @return
     */
    void moveSkill(Skill skill, Category category);


    /**
     * Deletes the given {@link Skill}.
     *
     * @param skill
     */
    void delete(Skill skill);


    /**
     * Returns all {@link MatrixTemplate}s.
     *
     * @return
     */
    List<MatrixTemplate> getTemplates();


    /**
     * Deletes a {@link MatrixTemplate}.
     *
     * @param template
     */
    void delete(MatrixTemplate template);


    /**
     * Saves a {@link MatrixTemplate}.
     *
     * @param template
     */
    MatrixTemplate save(MatrixTemplate template);


    /**
     * Returns a {@link MatrixTemplate} with the given id.
     *
     * @param id
     * @return
     */
    MatrixTemplate getTemplate(Long id);


    /**
     * Returns the {@link MatrixTemplate} to be assigned by default.
     *
     * @return
     */
    MatrixTemplate getDefaultTemplate();


    /**
     * Saves a given category.
     *
     * @param category
     */
    Category save(Category category);


    /**
     * @param category
     */
    void delete(Category category);


    /**
     * Saves the given {@link Responsibility}.
     *
     * @param responsibility
     * @return
     */
    Responsibility save(Responsibility responsibility);


    /**
     * Returns all {@link Responsibility}s.
     *
     * @return
     */
    List<Responsibility> getResponsibilities();


    /**
     * Returns all {@link Skill}s.
     *
     * @return
     */
    List<Skill> getSkills();
}
