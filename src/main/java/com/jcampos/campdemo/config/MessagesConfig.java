package com.jcampos.campdemo.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessagesConfig {


  public static final String CLASSPATH_MESSAGES = "classpath:messages";
  public static final String UTF_8 = "UTF-8";

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename(CLASSPATH_MESSAGES);
    messageSource.setDefaultEncoding(UTF_8);
    return messageSource;
  }


}
