package com.honeyai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for application configuration.
 * Covers: AC4 (1.1-INT-004, 1.1-INT-005)
 */
@SpringBootTest
class ConfigurationTest {

    @Autowired
    private DataSource dataSource;

    @Value("${server.port}")
    private int serverPort;

    @Test
    void dataSourceIsConfigured() throws SQLException {
        // Verify SQLite datasource is properly configured and connects
        assertThat(dataSource).isNotNull();
        assertThat(dataSource.getConnection()).isNotNull();
    }

    @Test
    void serverPortIs8080() {
        // Verify server port configuration matches AC4 requirement
        assertThat(serverPort).isEqualTo(8080);
    }
}
