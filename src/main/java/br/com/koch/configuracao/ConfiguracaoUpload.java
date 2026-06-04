package br.com.koch.configuracao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ConfiguracaoUpload implements WebMvcConfigurer {

    @Value("${koch.upload.dir:uploads/cestas}")
    private String diretorioUpload;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path pasta = Paths.get(diretorioUpload).toAbsolutePath().normalize();
        String location = pasta.toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/uploads/cestas/**")
                .addResourceLocations(location);
    }
}
