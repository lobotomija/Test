package hr.rba.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ResponseDTO implements Serializable {
    private static final long serialVersionID = 1L;

    @JsonProperty("message")
    @NonNull
    private String message;
}
