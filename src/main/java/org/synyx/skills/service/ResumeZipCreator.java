package org.synyx.skills.service;

import org.synyx.skills.domain.Level;
import org.synyx.skills.domain.Resume;

import java.io.File;
import java.io.OutputStream;

import java.util.List;


/**
 * Interface for creating a ZIP file from a {@link Resume}.
 *
 * @author Markus Knittig - knittig@synyx.de
 */
public interface ResumeZipCreator {

    /**
     * Creates a temporary (aka 'with a unique file name') ZIP {@link File} from a {@link Resume} instance.
     *
     * @param tempDirectory
     * @param resume
     * @param levels
     * @return
     * @throws DocbookCreationException
     */
    File createTempZipFile(File tempDirectory, Resume resume, List<Level> levels) throws ZipCreationException;


    /**
     * Creates a ZIP {@link OutputStream} from a {@link Resume} instance.
     *
     * @param resume
     * @param levels
     * @param outputStream
     * @throws ZipCreationException
     */
    void streamZip(Resume resume, List<Level> levels, OutputStream outputStream) throws ZipCreationException;
}
