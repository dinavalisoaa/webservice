package mg.mbds.webservice.responses;


public record ErrorData(String message){

    public static ErrorData from (Exception exception) {
        return new ErrorData(exception.getMessage());
    }
}
