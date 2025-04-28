package demacs.unical.esse20;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permetti le richieste da localhost:8080 (puoi cambiare l'URL se il tuo frontend è su un altro dominio)
        registry.addMapping("/api/**")  // Applica la configurazione solo agli endpoint della tua API
                .allowedOrigins("http://localhost:8080")  // URL del tuo frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Metodi HTTP consentiti
                .allowedHeaders("*")  // Consenti tutte le intestazioni
                .allowCredentials(true);  // Se hai bisogno di inviare cookie con le richieste
    }
}