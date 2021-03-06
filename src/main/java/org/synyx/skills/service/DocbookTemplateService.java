package org.synyx.skills.service;

import org.synyx.skills.domain.Level;
import org.synyx.skills.domain.Resume;

import java.util.List;


/**
 * Interface for creating Docbook XML based on a template.
 *
 * @author Markus Knittig - knittig@synyx.de
 */
public interface DocbookTemplateService {

    /**
     * Creates a Docbook XML from a {@link Resume}.
     *
     * @param resume The resume to fill the template
     * @param levels List of available levels
     * @param photoFilename The photoFilename that is used in the template
     * @return The Docbook XML as {@link String}
     * @throws DocbookCreationException
     */
    String createDocbookXml(Resume resume, List<Level> levels, String photoFilename, Boolean anonymous)
        throws DocbookCreationException;
}
