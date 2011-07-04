package org.synyx.skills.dao;

import org.synyx.skills.dao.CategoryDao;
import org.synyx.skills.dao.ResumeDao;
import org.synyx.skills.dao.SkillzDao;
import org.synyx.skills.dao.LevelDao;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.synyx.hades.domain.Pageable;
import org.synyx.hades.domain.Sort;
import org.synyx.minos.core.domain.User;
import org.synyx.skills.domain.Activity;
import org.synyx.skills.domain.Category;
import org.synyx.skills.domain.Level;
import org.synyx.skills.domain.MatrixTemplate;
import org.synyx.skills.domain.Resume;
import org.synyx.skills.domain.Skill;
import org.synyx.skills.domain.resume.CategoriesFilter;
import org.synyx.skills.domain.resume.ResumeFilter;
import org.synyx.skills.domain.resume.SkillLevelFilter;
import org.synyx.minos.test.AbstractDaoIntegrationTest;
import org.synyx.minos.umt.dao.UserDao;


/**
 * Integration test for {@link ResumeDao}.
 * 
 * @author Markus Knittig - knittig@synyx.de
 */
@Transactional
public class ResumeDaoIntegrationTest extends AbstractDaoIntegrationTest {

    @Autowired
    private ResumeDao resumeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SkillzDao skillzDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private LevelDao levelDao;

    private Pageable pageable;

    private Resume resume;
    private Category category;
    private Level level;


    @Before
    public void setUp() {

        User user = new User("username", "test@test.com", "password");
        userDao.save(user);
        category = new Category("categoryname");
        categoryDao.save(category);
        MatrixTemplate matrixTemplate = new MatrixTemplate("name");
        matrixTemplate.add(category);
        resume = new Resume(user, matrixTemplate, new ArrayList<Activity>());

        Skill skill = new Skill("skillname", category);
        skillzDao.save(skill);
        level = new Level("levelname", 0);
        levelDao.save(level);
        resume.getSkillz().add(skill, level);

        resumeDao.save(resume);

        pageable = mock(Pageable.class);
        when(pageable.getSort()).thenReturn(new Sort("lastModifiedDate"));
        when(pageable.getFirstItem()).thenReturn(0);
        when(pageable.getPageSize()).thenReturn(20);
    }


    @Test
    public void findByQueryWithSkillLevelFilter() throws Exception {

        ResumeFilter resumeFilter = new SkillLevelFilter(levelDao);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("skill", "skillname");
        parameters.put("level", level);

        List<Resume> resumes = resumeDao.findByFilter(pageable, resumeFilter, parameters);

        assertEquals(1, resumes.size());
    }


    @Test
    public void findByQueryWithCategoriesFilter() throws Exception {

        ResumeFilter resumeFilter = new CategoriesFilter(categoryDao);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("categories", Collections.singletonList(category));

        List<Resume> resumes = resumeDao.findByFilter(pageable, resumeFilter, parameters);

        assertEquals(1, resumes.size());
    }

}
