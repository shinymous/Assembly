package com.assembly.assembly.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;

@Service
public class SourceMessageService {

    private final MessageSource messageSource;

    public SourceMessageService(MessageSource messageSource){
        this.messageSource = messageSource;
    }
    public String getMessageFromUserLocale(String message) {
        return messageSource.getMessage(new DefaultMessageSourceResolvable(message), LocaleContextHolder.getLocale());
    }

    public String getMessageFromUserLocale(String message, Object... args) {
        return messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
    }
}
