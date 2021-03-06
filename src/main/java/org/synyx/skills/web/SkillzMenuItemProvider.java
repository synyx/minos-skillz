package org.synyx.skills.web;

import org.springframework.beans.factory.annotation.Autowired;

import org.synyx.minos.core.security.AuthenticationService;
import org.synyx.minos.core.web.menu.AbstractMenuItemProvider;
import org.synyx.minos.core.web.menu.MenuItem;
import org.synyx.minos.core.web.menu.UrlResolver;
import org.synyx.minos.core.web.menu.UserPlaceholderAwareUrlResolver;
import static org.synyx.skills.SkillzPermissions.SKILLZ_ADMINISTRATION;
import static org.synyx.skills.SkillzPermissions.SKILLZ_USER;

import java.util.Arrays;
import java.util.List;


/**
 * {@link org.synyx.minos.core.web.menu.MenuItemProvider} for Skillz module.
 *
 * @author Oliver Gierke - gierke@synyx.de
 */
public class SkillzMenuItemProvider extends AbstractMenuItemProvider {

    private static final String MENU_SKILLZ = "MENU_SKILLZ";
    private static final String MENU_SKILLZ_PRIVATEPROJECTS = "MENU_SKILLZ_PRIVATEPROJECTS";
    private static final String MENU_SKILLZ_RESUME = "MENU_SKILLZ_RESUME";
    private static final String MENU_SKILLZ_SKILLZ = "MENU_SKILLZ_SKILLZ";
    private static final String MENU_SKILLZ_RESUMES_MANAGE = "MENU_SKILLZ_RESUMES_MANAGE";

    @Autowired
    private AuthenticationService authenticationService;

    /*
     * (non-Javadoc)
     *
     * @see com.synyx.minos.core.web.menu.AbstractMenuItemProvider#initMenuItems()
     */
    @Override
    protected List<MenuItem> initMenuItems() {

        UrlResolver privateResumeStrategy = new UserPlaceholderAwareUrlResolver(String.format("/skillz/user/%s/resume",
                    UserPlaceholderAwareUrlResolver.DEFAULT_PLACEHOLDER), authenticationService);
        MenuItem skillzMenu = MenuItem.create(MENU_SKILLZ).withKeyBase("skillz.menu").withPosition(20)
                .withUrlResolver(privateResumeStrategy).withPermission(SKILLZ_USER).build();

        MenuItem manageResumes = MenuItem.create(MENU_SKILLZ_RESUMES_MANAGE).withKeyBase("skillz.menu.manageResumes")
            .withPosition(10).withUrl("/skillz/resumes").withPermission(SKILLZ_ADMINISTRATION).withParent(skillzMenu)
            .build();

        MenuItem skillz = MenuItem.create(MENU_SKILLZ_SKILLZ).withKeyBase("skillz.menu.skillz").withPosition(20)
            .withUrl("/skillz").withPermission(SKILLZ_ADMINISTRATION).withParent(skillzMenu).build();

        MenuItem resume = MenuItem.create(MENU_SKILLZ_RESUME).withKeyBase("skillz.menu.resume").withPosition(40)
            .withUrlResolver(privateResumeStrategy).withPermission(SKILLZ_USER).withParent(skillzMenu).build();

        UrlResolver privateProjectsStrategy = new UserPlaceholderAwareUrlResolver(String.format("/skillz/user/%s/projects",
                    UserPlaceholderAwareUrlResolver.DEFAULT_PLACEHOLDER), authenticationService);
        MenuItem privateProjects = MenuItem.create(MENU_SKILLZ_PRIVATEPROJECTS).withKeyBase(
                "skillz.menu.projects.private").withPosition(50).withUrlResolver(privateProjectsStrategy)
            .withPermission(SKILLZ_USER).withParent(skillzMenu).build();

        UrlResolver templateSwitchStrategy = new UserPlaceholderAwareUrlResolver(String.format("/skillz/user/%s/template",
                    UserPlaceholderAwareUrlResolver.DEFAULT_PLACEHOLDER), authenticationService);
        MenuItem templateSwitch = MenuItem.create(MENU_SKILLZ_PRIVATEPROJECTS).withKeyBase(
                "skillz.menu.template.switch").withPosition(50).withUrlResolver(templateSwitchStrategy)
            .withPermission(SKILLZ_USER).withParent(skillzMenu).build();

        return Arrays.asList(skillzMenu, manageResumes, skillz, resume, privateProjects, templateSwitch);
    }
}
