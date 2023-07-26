package lesson4.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cuisine",
        "cuisines",
        "confidence"
})
@Data
public class CuisineResponse {

    @JsonProperty("cuisine")
    private String cuisine;
    @JsonProperty("cuisines")
    private List<String> cuisines;
    @JsonProperty("confidence")
    private Double confidence;

}