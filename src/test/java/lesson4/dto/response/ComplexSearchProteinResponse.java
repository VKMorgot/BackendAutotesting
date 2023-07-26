package lesson4.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "results",
        "offset",
        "number",
        "totalResults"
})
@Data
public class ComplexSearchProteinResponse {

    @JsonProperty("results")
    private List<Result> results;
    @JsonProperty("offset")
    private Integer offset;
    @JsonProperty("number")
    private Integer number;
    @JsonProperty("totalResults")
    private Integer totalResults;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "id",
            "title",
            "image",
            "imageType",
            "nutrition"
    })
    @Data
    public static class Result {

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("image")
        private String image;
        @JsonProperty("imageType")
        private String imageType;
        @JsonProperty("nutrition")
        private Nutrition nutrition;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "name",
            "amount",
            "unit"
    })
    @Data
    public static class Nutrient {

        @JsonProperty("name")
        private String name;
        @JsonProperty("amount")
        private Float amount;
        @JsonProperty("unit")
        private String unit;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "nutrients"
    })
    @Data
    public static class Nutrition {

        @JsonProperty("nutrients")
        private List<Nutrient> nutrients;

    }
}



