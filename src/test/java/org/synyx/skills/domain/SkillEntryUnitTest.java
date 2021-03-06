package org.synyx.skills.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.synyx.skills.domain.Category;
import org.synyx.skills.domain.Level;
import org.synyx.skills.domain.Skill;
import org.synyx.skills.domain.SkillEntry;
import org.synyx.skills.domain.SkillMatrix;


/**
 * Unit test for {@link SkillEntry}.
 * 
 * @author Oliver Gierke - gierke@synyx.de
 */
public class SkillEntryUnitTest {

    private Category category;
    private Skill skill;
    private SkillMatrix matrix;
    private Level level;


    @Before
    public void setUp() {

        category = new Category("category");
        skill = new Skill("skill", category);
        matrix = new SkillMatrix();
        level = new Level("level", 0);
    }


    @Test
    public void hasSkillAndCategory() throws Exception {

        SkillEntry entry = new SkillEntry(skill, matrix, level);
        assertTrue(entry.has(skill));
        assertTrue(entry.has(category));
    }
}
