package de.gzockoll.prototype.jsf.entity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateTest {

    @Test
    public void testEquals() throws Exception {
        Template t1=new Template("de");
        Template t2=new Template("fr");
        Template t3=new Template("de");

        assertThat(t1.equals(t1)).isTrue();
        assertThat(t1.equals(t2)).isFalse();
        assertThat(t2.equals(t3)).isFalse();
        assertThat(t1.equals(t3)).isTrue();
        assertThat(t3.equals(t1)).isTrue();
    }

    @Test
    public void contains() {
        Template t1=new Template("de");
        Template t2=new Template("fr");
        Template t3=new Template("de");

        Collection<Template> collection=new ArrayList<>();
        collection.add(t1);
        assertThat(collection).contains(t1);
        assertThat(collection).contains(t3);
        assertThat(collection).doesNotContain(t2);

    }
}