package mg.mbds.webservice.responses;

public record ErrorResponse (ErrorData error) {

    public static ErrorResponse from(ErrorData error) {
        return new ErrorResponse(error);
    }
}
