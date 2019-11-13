package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Credibility {
    @JsonProperty("not verifiable post")
    not_verifiable_post,
    @JsonProperty("not credible post")
    not_credible_post,
    @JsonProperty("credible uncertain post")
    credible_uncertain_post,
    @JsonProperty("mostly credible post")
    mostly_credible_post,
    @JsonProperty("credible post")
    credible_post;

}
