package uz.pdp.userservice.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(
        HttpStatus status,
        boolean success,
        String message,
        Object data
) {
    public ApiResponse(HttpStatus status, boolean success, String message) {
      this(status,success,message,null);
    }


}
