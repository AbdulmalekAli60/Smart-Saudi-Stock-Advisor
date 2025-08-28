package com.SmartSaudiStockAdvisor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

@Configuration
public class LocalConfig {

    @Bean
    public LocaleResolver localeResolver(){
        FixedLocaleResolver localeResolver = new FixedLocaleResolver();
        localeResolver.setDefaultLocale(Locale.of("ar", "SA"));
        return localeResolver;
    }
}
