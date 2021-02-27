package com.assembly.assembly.conversors;

import com.assembly.assembly.exception.InvalidLocalDateTimeFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm][yyyy/MM/dd HH:mm][dd-MM-yyyy HH:mm][dd/MM/yyyy HH:mm]");

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try{
            return LocalDateTime.parse(jsonParser.getText(), dateTimeFormatter);
        }catch (DateTimeParseException e){
            try{
                return LocalDateTime.parse(jsonParser.getText());
            }catch (DateTimeParseException ex){
                throw new InvalidLocalDateTimeFormat("Invalid LocalDateTime format. " +
                        "Acceptable formats: [yyyy-MM-dd HH:mm][yyyy/MM/dd HH:mm][dd-MM-yyyy HH:mm][dd/MM/yyyy HH:mm]");
            }
        }
    }
}
