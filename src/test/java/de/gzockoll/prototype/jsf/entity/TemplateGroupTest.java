package de.gzockoll.prototype.jsf.entity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateGroupTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testDelegate() {
        TemplateGroup group=new TemplateGroup(4711,"Name","uk","JUnit");
        assertThat(group.getName()).isEqualTo("Name");
        assertThat(group.getQualifier()).isEqualTo("JUnit");
    }

    @Test
    public void testAddTemplates() {
        TemplateGroup group = new TemplateGroup(1, "test", "de", "junit");
        Template t = group.createTemplate();

        assertThat(group.getTemplates()).contains(t);
        assertThat(t.getGroup()).isEqualTo(group);
    }

    @Test
    public void isActive() {

        TemplateGroup group = new TemplateGroup(1, "test", "de", "junit");
        Template t = group.createTemplate();
        assertThat(t.isActive()).isFalse();
        group.addTemplate(t);
        assertThat(t.isActive()).isFalse();

        t.assignStationary(new Asset()).assignTransform("bla").requestApproval().approve();
        group.activate(t);
        assertThat(t.isActive()).isTrue();
    }

}