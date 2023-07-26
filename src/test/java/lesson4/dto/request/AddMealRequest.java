package lesson4.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "slot",
        "position",
        "type",
        "value"
})

@Data
public class AddMealRequest {

    @JsonProperty("date")
    private String date;
    @JsonProperty("slot")
    private String slot;
    @JsonProperty("position")
    private String position;
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Value value;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder( {
            "ingredients"
    })

    @Data
    public static class Value {
        @JsonProperty("ingredients")
        private List<Ingredient> ingredients = new ArrayList<>();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder( {"name"})
    @Data
    public static class Ingredient {
        @JsonProperty("name")
        private String name;
    }


}
