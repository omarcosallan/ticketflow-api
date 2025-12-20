package dev.marcos.ticketflow_api.dto.exception;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {

    private LocalDateTime timestamp;
    private String title;
    private String detail;
    private int status;
    private String instance;

    private Map<String, Object> properties;

    public ProblemDetail(String title, String detail, int status, String instance) {
        this.timestamp = LocalDateTime.now();
        this.title = title;
        this.detail = detail;
        this.status = status;
        this.instance = instance;
        this.properties = new HashMap<>();
    }

    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }
}
