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
public class CardRequestDTO implements Serializable {
    private static final long serialVersionID = 1L;

    @JsonProperty("firstName")
    @Size(max = 50)
    @NonNull
    private String firstName;

    @JsonProperty("lastName")
    @Size(max = 50)
    @NonNull
    private String lastName;

    @JsonProperty("oib")
    @Size(max = 11, min = 11)
    @NonNull
    private String oib;

    @JsonProperty("status")
    @Size(max = 100)
    private String status;
}
