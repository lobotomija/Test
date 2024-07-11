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
public class OibAndStatusDTO implements Serializable {
    private static final long serialVersionID = 1L;

    @JsonProperty("oib")
    @Size(max = 11)
    @NonNull
    private String oib;

    @JsonProperty("status")
    @Size(max = 100)
    @NonNull
    private String status;
}
