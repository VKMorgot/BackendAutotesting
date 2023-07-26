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
public class ComplexSearchDietResponse {

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
            "vegetarian",
            "vegan",
            "glutenFree",
            "dairyFree",
            "veryHealthy",
            "cheap",
            "veryPopular",
            "sustainable",
            "lowFodmap",
            "weightWatcherSmartPoints",
            "gaps",
            "preparationMinutes",
            "cookingMinutes",
            "aggregateLikes",
            "healthScore",
            "creditsText",
            "license",
            "sourceName",
            "pricePerServing",
            "id",
            "title",
            "readyInMinutes",
            "servings",
            "sourceUrl",
            "image",
            "imageType",
            "summary",
            "cuisines",
            "dishTypes",
            "diets",
            "occasions",
            "analyzedInstructions",
            "spoonacularSourceUrl"
    })

    @Data
    public static class Result {

        @JsonProperty("vegetarian")
        private Boolean vegetarian;
        @JsonProperty("vegan")
        private Boolean vegan;
        @JsonProperty("glutenFree")
        private Boolean glutenFree;
        @JsonProperty("dairyFree")
        private Boolean dairyFree;
        @JsonProperty("veryHealthy")
        private Boolean veryHealthy;
        @JsonProperty("cheap")
        private Boolean cheap;
        @JsonProperty("veryPopular")
        private Boolean veryPopular;
        @JsonProperty("sustainable")
        private Boolean sustainable;
        @JsonProperty("lowFodmap")
        private Boolean lowFodmap;
        @JsonProperty("weightWatcherSmartPoints")
        private Integer weightWatcherSmartPoints;
        @JsonProperty("gaps")
        private String gaps;
        @JsonProperty("preparationMinutes")
        private Integer preparationMinutes;
        @JsonProperty("cookingMinutes")
        private Integer cookingMinutes;
        @JsonProperty("aggregateLikes")
        private Integer aggregateLikes;
        @JsonProperty("healthScore")
        private Integer healthScore;
        @JsonProperty("creditsText")
        private String creditsText;
        @JsonProperty("license")
        private String license;
        @JsonProperty("sourceName")
        private String sourceName;
        @JsonProperty("pricePerServing")
        private Double pricePerServing;
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("readyInMinutes")
        private Integer readyInMinutes;
        @JsonProperty("servings")
        private Integer servings;
        @JsonProperty("sourceUrl")
        private String sourceUrl;
        @JsonProperty("image")
        private String image;
        @JsonProperty("imageType")
        private String imageType;
        @JsonProperty("summary")
        private String summary;
        @JsonProperty("cuisines")
        private List<Object> cuisines;
        @JsonProperty("dishTypes")
        private List<String> dishTypes;
        @JsonProperty("diets")
        private List<String> diets;
        @JsonProperty("occasions")
        private List<Object> occasions;
        @JsonProperty("analyzedInstructions")
        private List<?> analyzedInstructions;
        @JsonProperty("spoonacularSourceUrl")
        private String spoonacularSourceUrl;
    }
}

