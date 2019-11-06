package fi.laituri.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserOptions {
    @JsonProperty
    private String name;
    @JsonProperty
    private String character;
}
