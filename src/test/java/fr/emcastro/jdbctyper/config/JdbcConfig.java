package fr.emcastro.jdbctyper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import fr.emcastro.jdbctyper.JsonBoxTypeTransformer;
import fr.emcastro.jdbctyper.transform.TypeTransformerRegistry;

@Configuration
public class JdbcConfig {

    @PostConstruct
    public void registerTransformers() {
        TypeTransformerRegistry.register(new JsonBoxTypeTransformer());
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.duckdb.DuckDBDriver");
        dataSource.setUrl("jdbc:duckdb:");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource, ObjectMapper objectMapper) {
        

        return new JdbcTemplate(dataSource);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
