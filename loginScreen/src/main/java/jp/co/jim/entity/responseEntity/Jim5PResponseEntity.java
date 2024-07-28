package jp.co.jim.entity.responseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Jim5PResponseEntity {

    @JsonProperty("RESPONSE")
    private ResponseDetails responseDetails;

    // Getters and setters

    public static class ResponseDetails {

        @JsonProperty("CONTENTS")
        private Contents contents;

        @JsonProperty("HEADER")
        private Header header;

        // Getters and setters
    }

    public static class Contents {

        @JsonProperty("FILED1")
        private String field1;

        @JsonProperty("FIELD2")
        private String field2;

        @JsonProperty("FIELD3")
        private String field3;

        // Getters and setters
    }

    public static class Header {

        @JsonProperty("ERRORS")
        private Errors errors;

        // Getters and setters
    }

    public static class Errors {

        @JsonProperty("ERROR")
        private Error[] error;

        // Getters and setters
    }

    public static class Error {

        @JsonProperty("RETURN_CODE")
        private String returnCode;

        @JsonProperty("REASON_CODE")
        private String reasonCode;

        // Getters and setters
    }

    // Getters and setters for the outer Response class
}
