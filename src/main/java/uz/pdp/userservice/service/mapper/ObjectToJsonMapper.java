package uz.pdp.userservice.service.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import uz.pdp.userservice.exception.JsonProcessingException;

import java.util.function.Function;

@Service
public class ObjectToJsonMapper implements Function<Object, String> {

    @Override
    public String apply(Object object) {
        // Create an instance of the ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        String json;

        try {
            // Convert the DTO to JSON string
            json = objectMapper.writeValueAsString(object);
            System.out.println(json);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new JsonProcessingException("Error while converting DTO to JSON string");
        }
        return json;
    }
}
