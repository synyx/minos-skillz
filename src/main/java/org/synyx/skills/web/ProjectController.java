package org.synyx.skills.web;

import javax.annotation.security.RolesAllowed;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.synyx.minos.core.Core;
import org.synyx.minos.core.domain.User;
import org.synyx.minos.core.web.CurrentUser;
import org.synyx.minos.core.web.Message;
import org.synyx.minos.core.web.UrlUtils;
import org.synyx.minos.umt.service.UserManagement;
import org.synyx.skills.SkillzPermissions;

import org.synyx.skills.domain.Project;
import org.synyx.skills.service.SkillManagement;
import org.synyx.skills.service.SkillsAuthenticationServiceWrapper;
import org.synyx.skills.web.validation.ProjectValidator;

/**
 *  Controller handling the management of public and private projects.
 *
 * @author <a href="mailto:menz@synyx.de">Alexander Menz</a> - <a href="http://www.synyx.de">Synyx GmbH & Co. KG</a>
 */
@Controller
public class ProjectController {

    private static final String SKILLS_PUBLIC_PROJECTS = "/skillz#tabs-3";

    private SkillsAuthenticationServiceWrapper authenticationService = null;

    private SkillManagement skillManagement = null;
    private ProjectValidator projectValidator = null;

    /**
     * Standard constructor just for enabling AOP.
     */
    protected ProjectController() { }

    /**
     * Creates a new instance of {@link ProjectController}.
     * 
     * @param skillManagement
     * @param userManagement
     * @param projectValidator
     * @param authenticationService
     */
    @Autowired
    public ProjectController(SkillManagement skillManagement, ProjectValidator projectValidator, SkillsAuthenticationServiceWrapper authenticationService) {

        this.skillManagement = skillManagement;
        this.projectValidator = projectValidator;
        this.authenticationService = authenticationService;
    }

    /**
     * List a users private projects. Only the admin may list the private projects of another
     * user (different from currently logged in user).
     *
     * @param username
     * @param model
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_USER, SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = { "/skillz/user/{username:[a-zA-Z_]\\w*}/projects" }, method = GET)
    public String privateProjectList(@PathVariable("username") String username, Model model, @CurrentUser User currentUser) {

        User user = authenticationService.getUserIfCurrentUserOrAdmin(username);

        model.addAttribute("projects", skillManagement.getPrivateProjectsFor(user));
        model.addAttribute("username", username);

        return "skillz/projects";
    }

    /**
     * Show form for creation/editing a private project. Only the admin may create/edit private projects of
     * another user (different from currently logged in user).
     *
     * @param username username of the user to create/edit the project for
     * @param project the project to edit or {@code null} to create a new project
     * @param model
     * @param currentUser
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_USER, SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = { "/skillz/user/{username:[a-zA-Z_]\\w*}/projects/{id:\\d+}" }, method = GET)
    public String privateProjectCreateOrEdit(@PathVariable("username") String username, @PathVariable("id") Project project, Model model, @CurrentUser User currentUser) {

        User user = authenticationService.getUserIfCurrentUserOrAdmin(username);

        // if editing existing project, check if the chosen (via path, not necessarily equal to the logged in) user
        // is the owner of that project
        if (project != null && !project.belongsTo(user)) {

            throw new IllegalArgumentException("Chosen project does not belong to chosen user.");
        }

        // if creating a new project, prefill the owner
        Project projectToEdit = project;
        if (null == projectToEdit) {
            projectToEdit = BeanUtils.instantiateClass(Project.class);
            projectToEdit.setOwner(user);
        }

        model.addAttribute("project", projectToEdit);

        return "skillz/project";
    }

    /**
     * Shows the form for creating a new private project.
     *
     * @param username
     * @param model
     * @param user
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_USER, SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = { "/skillz/user/{username:[a-zA-Z_]\\w*}/projects/form" }, method = GET)
    public String privateProjectForm(@PathVariable("username") String username, Model model, @CurrentUser User currentUser) {

        return privateProjectCreateOrEdit(username, null, model, currentUser);
    }

    /**
     * Saves a new private project.
     *
     * @param username
     * @param project
     * @param errors
     * @param model
     * @param currentUser
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_USER, SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = { "/skillz/user/{username:[a-zA-Z_]\\w*}/projects" }, method = POST)
    public String saveNewPrivateProject(@PathVariable("username") String username, @ModelAttribute("project") Project project, Errors errors, Model model, @CurrentUser User currentUser) {

        // Note: If you're editing this, keep in mind that saveExistingPrivateProject(..)
        // delegates to this method (just added for different request mapping)

        // only admin may save a new  project for other users
        User user = authenticationService.getUserIfCurrentUserOrAdmin(username);

        if (!project.belongsTo(user)) {

            throw new IllegalArgumentException("Project to save does not belong to chosen user.");
        }

        // validate project
        projectValidator.validate(project, errors);

        if (errors.hasErrors()) {

            return privateProjectCreateOrEdit(username, project, model, currentUser);
        }

        saveProject(project, model);

        return UrlUtils.redirect("/web/skillz/user/" + project.getOwner().getUsername() +"/projects");
    }

    /**
     * Saves an existing private project.
     *
     * @param username
     * @param project
     * @param errors
     * @param model
     * @param currentUser
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_USER, SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = { "/skillz/user/{username:[a-zA-Z_]\\w*}/projects/{id:\\d+}" }, method = PUT)
    public String saveExistingPrivateProject(@PathVariable("username") String username, @ModelAttribute("project") Project project, Errors errors, Model model, @CurrentUser User currentUser) {

        return saveNewPrivateProject(username, project, errors, model, currentUser);
    }

    /**
     * Deletes a private project.
     *
     * @param username
     * @param project
     * @param model
     * @param currentUser
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_USER, SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = { "/skillz/user/{username:[a-zA-Z_]\\w*}/projects/{id:\\d+}" }, method = DELETE)
    public String deletePrivateProject(@PathVariable("username") String username, @PathVariable("id") Project project, Model model, @CurrentUser User currentUser) {

        User user = authenticationService.getUserIfCurrentUserOrAdmin(username);

        // check if project exists and belongs to designated user
        if (null == project || !project.belongsTo(user)) {

            throw new IllegalArgumentException("Chosen project does not exist or belong to chosen user.");
        }

        skillManagement.delete(project);

        model.addAttribute(Core.MESSAGE, Message.success("skillz.project.delete.success", project.getName()));

        return UrlUtils.redirect("/web/skillz/user/" + project.getOwner().getUsername() + "/projects");
    }


    /**
     * List public projects. Only the admin may list private projects.
     *
     * @param model
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = { "/skillz/projects" }, method = GET)
    public String publicProjectList(Model model) {

        model.addAttribute("projects", skillManagement.getPublicProjects());

        return "skillz/projects";
    }

    /**
     * Show form for creation/editing a public project. Only the admin may create/edit public projects.
     *
     * @param project the project to edit or {@code null} to create a new project
     * @param model
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = { "/skillz/projects/{id:\\d+}" }, method = GET)
    public String publicProjectCreateOrEdit(@PathVariable("id") Project project, Model model) {

        model.addAttribute("project", project != null ? project : BeanUtils.instantiateClass(Project.class));

        return "skillz/project";
    }

    /**
     * Shows the form for creating a new public project. Only admin may create new public projects.
     *
     * @param model
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = { "/skillz/projects/form" }, method = GET)
    public String publicProjectForm(Model model) {

        return publicProjectCreateOrEdit(null, model);
    }

    /**
     * Saves a new public project. Only admin may save new public projects.
     *
     * @param project
     * @param errors
     * @param model
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = { "/skillz/projects" }, method = POST)
    public String saveNewPublicProject(@ModelAttribute("project") Project project, Errors errors, Model model) {

        // Note: If you're editing this, keep in mind that saveExistingPublicProject(..)
        // delegates to this method (just added for different request mapping)

        // validate project
        projectValidator.validate(project, errors);

        if (errors.hasErrors()) {
            return "skillz/project";
        }

        // validation was successful
        saveProject(project, model);

        return UrlUtils.redirect(SKILLS_PUBLIC_PROJECTS);
    }



    /**
     * Saves an existing public project. Only admin may save public projects.
     *
     * @param project
     * @param errors
     * @param model
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = "/skillz/projects/{id:\\d+}", method = PUT)
    public String saveExistingPublicProject(@ModelAttribute("project") Project project, Errors errors, Model model) {

        return saveNewPublicProject(project, errors, model);
    }

    /**
     * Deletes a public project. Only admin may delete public projects.
     *
     * @param project
     * @param model
     * @return
     */
    @RolesAllowed({SkillzPermissions.SKILLZ_ADMINISTRATION})
    @RequestMapping(value = "/skillz/projects/{id:\\d+}", method = DELETE)
    public String deleteProject(@PathVariable("id") Project project, Model model) {

        skillManagement.delete(project);

        model.addAttribute(Core.MESSAGE, Message.success("skillz.project.delete.success", project.getName()));

        return UrlUtils.redirect(SKILLS_PUBLIC_PROJECTS);
    }

    /**
     * Invokes the service layer to save the given project.
     *
     * @param project
     * @param model
     */
    private void saveProject(Project project, Model model) {

        project = skillManagement.save(project);

        model.addAttribute(Core.MESSAGE, Message.success("skillz.project.save.success", project.getName()));
    }
}
