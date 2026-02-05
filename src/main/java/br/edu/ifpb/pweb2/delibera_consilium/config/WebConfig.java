package br.edu.ifpb.pweb2.delibera_consilium.config;

import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuracao do Spring MVC para conversao de tipos
 * Resolve o binding de IDs para entidades nos formularios
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ProfessorRepository professorRepository;

    public WebConfig(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToProfessorConverter());
    }

    /**
     * Converter para transformar String (ID) em Professor
     * Usado no binding do campo 'membros' no formulario de reuniao
     */
    private class StringToProfessorConverter implements Converter<String, Professor> {
        @Override
        public Professor convert(String source) {
            if (source == null || source.isEmpty()) {
                return null;
            }
            try {
                Long id = Long.parseLong(source);
                return professorRepository.findById(id).orElse(null);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
