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
public class ErrorResponseDTO implements Serializable {
    private static final long serialVersionID = 1L;

    @JsonProperty("code")
    @Size(max = 10)
    @NonNull
    private String code;

    @JsonProperty("id")
    @Size(max = 10)
    @NonNull
    private String id;

    @JsonProperty("description")
    @Size(max = 100)
    @NonNull
    private String description;
}
