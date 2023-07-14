package uz.pdp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.pdp.userservice.payload.ApiResponse;
import uz.pdp.userservice.payload.SendEMailDTO;
import uz.pdp.userservice.service.mapper.ObjectToJsonMapper;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ObjectToJsonMapper objectToJsonMapper;

    @Value("${services.notification-url}")
    private String notificationServiceUrl; //= "http://localhost:8082";

    public void sendSimpleEmail(SendEMailDTO sendEMailDTO) {
        ResponseEntity<ApiResponse> response = sendEmail(sendEMailDTO, "/send-simple-email");
        if (!response.getBody().success()) {
            throw new RuntimeException("Email not sent");
        };
    }


    public void sendHtmlEmail(SendEMailDTO sendEMailDTO) {
        ResponseEntity<ApiResponse> response = sendEmail(sendEMailDTO, "/send-html-template");
        if (!response.getBody().success()) {
            throw new RuntimeException("Email not sent");
        };
    }

    private ResponseEntity<ApiResponse> sendEmail(SendEMailDTO sendEMailDTO, String x) {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Set the request headers, if necessary
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body
        String requestBody = objectToJsonMapper.apply(sendEMailDTO);

        // Create the HTTP entity with headers and body
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send a POST request to the Notification Service through the API Gateway
        return restTemplate.exchange(
                notificationServiceUrl + x,
                HttpMethod.POST,
                entity,
                ApiResponse.class);
    }
}
