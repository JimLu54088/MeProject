package jp.co.jim.entity.responseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Jim5PResponseEntity {

    @JsonProperty("RESPONSE")
    private ResponseDetails responseDetails;

    // Getters and setters
    @Data
    public static class ResponseDetails {

        @JsonProperty("CONTENTS")
        private Contents CONTENTS;

        @JsonProperty("HEADER")
        private Header HEADER;

        // Getters and setters
    }

    @Data
    public static class Contents {

        @JsonProperty("FILED1")
        private String FILED1;

        @JsonProperty("FIELD2")
        private String FIELD2;

        @JsonProperty("FIELD3")
        private String FIELD3;

        // Getters and setters
    }

    @Data
    public static class Header {

        @JsonProperty("ERRORS")
        private Errors ERRORS;

        // Getters and setters
    }

    @Data
    public static class Errors {

        @JsonProperty("ERROR")
        private Error[] ERROR;

        // Getters and setters
    }

    @Data
    public static class Error {

        @JsonProperty("RETURN_CODE")
        private String RETURN_CODE;

        @JsonProperty("REASON_CODE")
        private String REASON_CODE;

        // Getters and setters
    }

    // Getters and setters for the outer Response class
}
