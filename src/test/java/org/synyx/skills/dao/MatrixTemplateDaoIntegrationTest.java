package org.synyx.skills.dao;

import org.synyx.skills.dao.MatrixTemplateDao;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.synyx.skills.domain.MatrixTemplate;
import org.synyx.minos.test.AbstractDaoIntegrationTest;


/**
 * Integration test for {@link MatrixTemplateDao}.
 * 
 * @author Oliver Gierke - gierke@synyx.de
 */
public class MatrixTemplateDaoIntegrationTest extends AbstractDaoIntegrationTest {

    @Autowired
    private MatrixTemplateDao templateDao;


    @Before
    public void setUp() {

        for (int i = 0; i < 10; i++) {

            MatrixTemplate template = new MatrixTemplate("Template" + i);

            if (i == 0) {
                template.setDefault(true);
            }

            templateDao.saveAndFlush(template);
        }
    }


    @Test
    public void undefaultsTemplatesCorrectly() throws Exception {

        MatrixTemplate template = templateDao.findByName("Template8");
        template.setDefault(true);

        templateDao.undefaultAllBut(template);
        templateDao.save(template);

        assertTrue(templateDao.readByPrimaryKey(template.getId()).isDefault());
    }
}
