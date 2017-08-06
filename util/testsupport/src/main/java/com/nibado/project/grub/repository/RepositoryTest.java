package com.nibado.project.grub.repository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class RepositoryTest<T> {
    private final String table;

    protected EmbeddedDatabase database;
    protected JdbcTemplate template;


    public RepositoryTest(final String table) {
        this.table = table;
    }

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setup() {
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create.sql")
                .build();

        template = new JdbcTemplate(database);

        JdbcTestUtils.deleteFromTables(template, table);

        assertEmpty();
    }

    protected void assertRows(int amount) {
        assertThat(JdbcTestUtils.countRowsInTable(template, table)).isEqualTo(amount);
    }

    protected void assertEmpty() {
        assertRows(0);
    }
}
