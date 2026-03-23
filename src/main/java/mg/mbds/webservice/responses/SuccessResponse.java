package mg.mbds.webservice.responses;
public record SuccessResponse<T>(T data) {
    public SuccessResponse(T data) {
        this.data = data;
    }

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<T>(data);
    }

    public T data() {
        return this.data;
    }
}
