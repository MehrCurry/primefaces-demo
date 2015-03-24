package de.gzockoll.prototype.jsf.entity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateGroupTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testAddTemplates() {
        Template t = new Template("de");
        TemplateGroup group = new TemplateGroup(1, "de", "junit");
        group.addTemplate(t);
        assertThat(group.getTemplates()).contains(t);
        assertThat(t.getGroup()).isEqualTo(group);
    }

    @Test
    public void testHasSameLanguage() {
        Template t = new Template("de");
        TemplateGroup group = new TemplateGroup(1, "en", "junit");
        thrown.expect(IllegalArgumentException.class);
        t.setGroup(group);
    }

    @Test
    public void isActive() {
        Template t = new Template("de");
        assertThat(t.isActive()).isFalse();

        TemplateGroup group = new TemplateGroup(1, "de", "junit");
        group.addTemplate(t);
        assertThat(t.isActive()).isFalse();

        t.assignStationary(new Asset()).assignTransform("bla").requestApproval().approve();
        group.activate(t);
        assertThat(t.isActive()).isTrue();
    }

}