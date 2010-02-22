package org.synyx.minos.skillz.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.Fop;
import org.springframework.util.Assert;
import org.synyx.minos.core.domain.Image;
import org.synyx.minos.skillz.domain.Level;
import org.synyx.minos.skillz.domain.Resume;


/**
 * Implementation of {@link PdfDocbookCreator}. Uses Saxon's implementation of
 * {@link TransformerFactory}, as the default JDK {@link TransformerFactory}
 * doesn't work.
 * 
 * @author Markus Knittig - knittig@synyx.de
 */
public class PdfDocbookCreatorImpl implements PdfDocbookCreator {

    private final DocbookTemplateService docbookTemplateService;
    private final FopXsltService fopService;


    public PdfDocbookCreatorImpl(DocbookTemplateService docbookTemplateService,
            FopXsltService fopService) {

        this.docbookTemplateService = docbookTemplateService;
        this.fopService = fopService;
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.synyx.minos.skillz.service.PdfDocbookCreator#streamPdf(java.lang.
     * String, java.io.File, java.io.OutputStream)
     */
    @Override
    public void streamPdf(Resume resume, List<Level> levels,
            OutputStream outputStream) throws DocbookCreationException {

        File tmpPhotoFile = null;
        String tmpPhotoFileName = null;
        if (resume.getPhoto() != null) {
            tmpPhotoFile = createTmpPhotoFile(resume.getPhoto());
            tmpPhotoFileName = tmpPhotoFile.getAbsolutePath();
        }

        String docbookXml =
                docbookTemplateService.createDocbookXml(resume, levels,
                        tmpPhotoFileName);
        streamPdf(docbookXml, null, outputStream);

        if (tmpPhotoFile != null) {
            tmpPhotoFile.delete();
        }
    }


    /**
     * Creates a photo file in the system's tmp directory.
     * 
     * @param image
     * @return
     * @throws DocbookCreationException
     */
    private File createTmpPhotoFile(Image image)
            throws DocbookCreationException {

        File tmpPhotoFile = null;
        try {
            tmpPhotoFile = File.createTempFile("photo", null);
            IOUtils.write(image.getOriginalImage(), new FileOutputStream(
                    tmpPhotoFile));
        } catch (Exception e) {
            throw new DocbookCreationException(
                    "Failed to create temporary photo file!", e);
        }
        return tmpPhotoFile;
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.synyx.minos.skillz.service.PdfDocbookCreator#streamPdf(org.synyx.
     * minos.skillz.domain.Resume, java.io.OutputStream)
     */
    @Override
    public void streamPdf(String xmlString, File xsltFile,
            OutputStream outputStream) throws DocbookCreationException {

        Assert.notNull(xmlString);
        Assert.notNull(outputStream);

        try {
            Fop fop = fopService.createFop(outputStream);

            Transformer transformer = fopService.createTransformer(xsltFile);

            Source source = new StreamSource(new StringReader(xmlString));

            Result result = new SAXResult(fop.getDefaultHandler());

            transformer.transform(source, result);
        } catch (Exception e) {
            throw new DocbookCreationException(
                    "Failed to apply FOP XSLT transformation!", e);
        }
    }

}
