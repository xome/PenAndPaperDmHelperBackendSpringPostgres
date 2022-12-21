package de.mayer.backendspringpostgres.adventure.service.api.jacksonMixIn;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.model.Chapter;
import de.mayer.backendspringpostgres.adventure.service.api.jacksonMixIn.ChapterMixIn;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonObjectMapper {

    @Bean
    @Primary
    public ObjectMapper objectMapper(){
        return new ObjectMapper()
                .addMixIn(Chapter.class, ChapterMixIn.class)
                ;
    }

}
