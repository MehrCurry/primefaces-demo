package de.gzockoll.prototype.jsf.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LanguageCodeTest {

    @Test
    public void testEquals() throws Exception {
        LanguageCode l1=new LanguageCode("de");
        LanguageCode l2=new LanguageCode("us");
        LanguageCode l3=new LanguageCode("de");

        assertThat(l1.equals(l1)).isTrue();
        assertThat(l1.equals(l2)).isFalse();
        assertThat(l2.equals(l3)).isFalse();
        assertThat(l1.equals(l3)).isTrue();
        assertThat(l3.equals(l1)).isTrue();
    }
}