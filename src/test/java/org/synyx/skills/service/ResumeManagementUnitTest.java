package org.synyx.skills.service;

import org.synyx.skills.service.ResumeManagementImpl;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;
import org.synyx.minos.core.domain.User;
import org.synyx.skills.dao.ActivityDao;
import org.synyx.skills.dao.LevelDao;
import org.synyx.skills.dao.MatrixTemplateDao;
import org.synyx.skills.dao.ResumeDao;
import org.synyx.skills.dao.SkillzMatrixDao;
import org.synyx.skills.domain.Activity;
import org.synyx.skills.domain.Category;
import org.synyx.skills.domain.Level;
import org.synyx.skills.domain.MatrixTemplate;
import org.synyx.skills.domain.Resume;
import org.synyx.skills.domain.Skill;
import org.synyx.skills.domain.resume.ResumeAttributeFilter;
import org.synyx.skills.domain.resume.ResumeFilter;
import org.synyx.skills.domain.resume.SkillLevelFilter;


/**
 * Unit test for {@code ResumeManagementImpl}.
 * 
 * @author Markus Knittig - knittig@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class ResumeManagementUnitTest {

    private ResumeManagementImpl resumeManagement;
    @Mock
    private ResumeDao resumeDao;
    @Mock
    private LevelDao levelDao;
    @Mock
    private ConversionService conversionService;
    @Mock
    private Pageable pageable;


    private Resume resume;


    @Before
    public void setUp() {

        MatrixTemplateDao matrixTemplateDao = mock(MatrixTemplateDao.class);

        when(matrixTemplateDao.findDefault()).thenReturn(new MatrixTemplate("dummy"));

        resumeManagement =
                new ResumeManagementImpl(resumeDao, mock(ActivityDao.class), mock(SkillzMatrixDao.class),
                        matrixTemplateDao, conversionService);

        User user = new User("username", "test@test.com", "password");
        resume = new Resume(user, new MatrixTemplate("name"), new ArrayList<Activity>());
        Category category = new Category("categoryname");
        Skill skill = new Skill("skillname", category);
        Level level = new Level("levelname", 2);
        resume.getSkillz().add(skill, level);
    }


    @SuppressWarnings("unchecked")
    @Test
    public void getsFilteredResumes() throws Exception {

        ResumeFilter resumeQuery = new SkillLevelFilter(levelDao);
        Map<String, String[]> parameters = new HashMap<String, String[]>();
        parameters.put("skill", new String[] { "levelname" });
        parameters.put("level", new String[] { "1" });
        List<Resume> resumes = new ArrayList<Resume>();
        resumes.add(resume);
        when(conversionService.convert(eq("name"), eq(String.class))).thenReturn("name");
        when(conversionService.convert(eq("1"), eq(Integer.class))).thenReturn(1);
        when(resumeDao.findByFilter(eq(pageable), (ResumeFilter) anyObject(), (Map<String, Object>) anyObject()))
                .thenReturn(resumes);
        when(pageable.getPageNumber()).thenReturn(0);
        when(pageable.getPageSize()).thenReturn(20);

        Page<Resume> resumePage = resumeManagement.getResumesByFilter(pageable, resumeQuery, parameters);

        assertEquals(1, resumePage.getNumberOfElements());
    }


    @Ignore("Does not test anything useful. Needs to be corrected!")
    @Test
    public void filtersResumeAttributes() throws Exception {

        ResumeAttributeFilter filter = mock(ResumeAttributeFilter.class);
        when(resumeManagement.getResume((User) anyObject())).thenReturn(resume);

        resumeManagement.getFilteredResume(new User("john doe", "john@example.org"), Collections.singletonList(filter));

        verify(filter).filter(eq(resume));
    }

}
