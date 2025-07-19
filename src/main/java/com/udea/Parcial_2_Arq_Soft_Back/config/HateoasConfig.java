package com.udea.Parcial_2_Arq_Soft_Back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import com.udea.Parcial_2_Arq_Soft_Back.model.entity.Doctor;
import com.udea.Parcial_2_Arq_Soft_Back.model.entity.Paciente;
import com.udea.Parcial_2_Arq_Soft_Back.model.entity.HistoriaClinica;

@Configuration
public class HateoasConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // Exponer IDs en las respuestas JSON
        config.exposeIdsFor(Doctor.class, Paciente.class, HistoriaClinica.class);

        // Configurar CORS para Spring Data REST
        cors.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}