package fr.example.springjdbccli.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

@Configuration
public class JdbcConfig {

    // @Bean
    // public DataSource dataSource() {
    //     DriverManagerDataSource dataSource = new DriverManagerDataSource();
    //     dataSource.setDriverClassName("org.postgresql.Driver");
    //     dataSource.setUrl("jdbc:postgresql://localhost:5432/ma_base");
    //     dataSource.setUsername("mon_utilisateur");
    //     dataSource.setPassword("mon_mot_de_passe");
    //     return dataSource;
    // }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource, ObjectMapper objectMapper) {
        

        return new JdbcTemplate(dataSource);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
