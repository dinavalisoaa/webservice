package mg.mbds.webservice.responses;

import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<SuccessResponse<T>> success(T data) {
        return ResponseEntity.ok(new SuccessResponse<>(data));
    }

}
