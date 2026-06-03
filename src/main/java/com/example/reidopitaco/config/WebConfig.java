package com.example.reidopitaco.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Faz a conversão de query/path params de String para enum ser <b>case-insensitive</b>
 * (ex.: {@code ?scope=mine} casa com {@code TeamScope.MINE}). Vale para todos os enums.
 * Valor inválido lança {@code IllegalArgumentException}, que o Spring envelopa em
 * {@code MethodArgumentTypeMismatchException} → tratado como 400 no GlobalExceptionHandler.
 */
@Configuration
@EnableConfigurationProperties(AvatarProperties.class)
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CaseInsensitiveEnumConverterFactory());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static class CaseInsensitiveEnumConverterFactory implements ConverterFactory<String, Enum> {

        @Override
        public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
            return source -> {
                String value = source.trim();
                for (T constant : targetType.getEnumConstants()) {
                    if (constant.name().equalsIgnoreCase(value)) {
                        return constant;
                    }
                }
                throw new IllegalArgumentException(
                        "No enum constant " + targetType.getCanonicalName() + " for value '" + source + "'"
                );
            };
        }
    }
}
