package org.synyx.skills.service;

import org.synyx.skills.service.DocbookTemplateService;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.synyx.minos.core.domain.User;
import org.synyx.skills.domain.Activity;
import org.synyx.skills.domain.Category;
import org.synyx.skills.domain.Level;
import org.synyx.skills.domain.MatrixTemplate;
import org.synyx.skills.domain.Resume;
import org.synyx.skills.domain.Skill;


/**
 * @author Markus Knittig - knittig@synyx.de
 */
public abstract class AbstractDocbookCreatorIntegrationTest {

    protected DocbookTemplateService docbookTemplateService;
    protected Resume resume;


    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {

        User user = new User("username", "test@test.com", "password");
        resume = new Resume(user, new MatrixTemplate("name"), new ArrayList<Activity>());
        Category category = new Category("categoryname");
        Skill skill = new Skill("skillname", category);
        Level level = new Level("levelname", 0);
        resume.getSkillz().add(skill, level);
        resume.setTitle("Dr.");

        docbookTemplateService = mock(DocbookTemplateService.class);
        when(docbookTemplateService.createDocbookXml((Resume) anyObject(), (List<Level>) anyObject(), anyString(), anyBoolean()))
                .thenReturn(IOUtils.toString(new ClassPathResource("/docbookexample.xml").getInputStream()));
    }

}
