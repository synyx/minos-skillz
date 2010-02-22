package org.synyx.minos.skillz.service;

import java.io.OutputStream;
import java.util.List;

import org.synyx.minos.skillz.domain.Level;
import org.synyx.minos.skillz.domain.Resume;


/**
 * Interface for creating a ZIP file from a {@link Resume}.
 * 
 * @author Markus Knittig - knittig@synyx.de
 */
public interface ResumeZipCreator {

    /**
     * Creates a ZIP {@link OutputStream} from a {@link Resume} instance.
     * 
     * @param resume
     * @param levels
     * @param outputStream
     * @throws ZipCreationException
     */
    void streamZip(Resume resume, List<Level> levels, OutputStream outputStream)
            throws ZipCreationException;

}
