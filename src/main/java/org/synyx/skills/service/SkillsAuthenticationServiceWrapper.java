package org.synyx.skills.service;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.access.AccessDeniedException;
import org.synyx.minos.core.domain.Password;
import org.synyx.minos.core.domain.User;
import org.synyx.minos.core.security.AuthenticationService;
import org.synyx.minos.umt.service.UserManagement;
import org.synyx.skills.SkillzPermissions;

/**
 * Wrapper for the {@link AuthenticationService} adding "Skills" specific convenience and
 * helper methods for repeatedly occuring authentication checks.
 *
 * @author <a href="mailto:menz@synyx.de">Alexander Menz</a> - <a href="http://www.synyx.de">Synyx GmbH & Co. KG</a>
 */
public class SkillsAuthenticationServiceWrapper implements AuthenticationService {

    private AuthenticationService wrappedAuthenticationService = null;
    private UserManagement userManagement = null;

    /**
     * Creates a new instance of {@link SkillsAuthenticationServiceWrapper} wrapping the given
     * instance of {@link AuthenticationService}. The given instance of {@link UserManagement}
     * is needed to provide the additional "Skills" specific functionality.
     *
     * @param authenticationService
     */
    public SkillsAuthenticationServiceWrapper(AuthenticationService authenticationService, UserManagement userManagement) {

        this.wrappedAuthenticationService = authenticationService;
        this.userManagement = userManagement;
    }

    /**
     * Get the user for the given {@code username} if the user represented is the current logged  in user
     * or the current logged in user owns the permission {@link SkillzPermissions.SKILLZ_ADMINISTRATION}.
     * <p>
     * If there is no user bearing the given {@code username}, an {@link IllegalArgumentException}
     * is thrown. If the requested user does neither resemble the current logged in user nor owns
     * the permission {@link SkillzPermissions.SKILLZ_ADMINISTRATION}, an {@link AccessDeniedException}
     * is thrown.
     * </p>
     *
     * @param username
     * @return
     */
    public User getUserIfCurrentUserOrAdmin(String username) {

        // determine chosen user
        User user = userManagement.getUser(username);
        if (null == user) {

            throw new IllegalArgumentException("Chosen user does not exist.");
        }

        // only admin may handle other users data
        if (!isCurrentUser(user)) {
            // other user than currently logged in user
            if (!hasAllPermissions(Arrays.asList(SkillzPermissions.SKILLZ_ADMINISTRATION))) {
                throw new AccessDeniedException("Only administrator may act as another user.");
            }
        }

        return user;
    }

    //
    // Methods declared by the AuthenticationService interface. Just delegating to the underlying
    // implementation.
    //

    @Override
    public User getCurrentUser() {

        return this.wrappedAuthenticationService.getCurrentUser();
    }

    @Override
    public boolean isCurrentUser(String username) {

        return this.wrappedAuthenticationService.isCurrentUser(username);
    }

    @Override
    public boolean isCurrentUser(User user) {

        return this.wrappedAuthenticationService.isCurrentUser(user);
    }

    @Override
    public boolean hasAnyPermission(Collection<String> permissions) {

        return this.wrappedAuthenticationService.hasAnyPermission(permissions);
    }

    @Override
    public boolean hasAllPermissions(Collection<String> permissions) {

        return this.wrappedAuthenticationService.hasAllPermissions(permissions);
    }

    @Override
    public Password getEncryptedPasswordFor(User user) {

        return this.wrappedAuthenticationService.getEncryptedPasswordFor(user);
    }

    @Override
    public Collection<String> getPermissions() {

        return this.wrappedAuthenticationService.getPermissions();
    }


}
