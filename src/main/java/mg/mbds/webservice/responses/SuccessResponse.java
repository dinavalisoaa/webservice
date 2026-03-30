package mg.mbds.webservice.responses;

import lombok.Getter;

@Getter
public class SuccessResponse<T> {

    private final T data;

    public SuccessResponse(T data) {
        this.data = data;
    }

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(data);
    }

}