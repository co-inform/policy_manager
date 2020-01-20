package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Credibility {
    @JsonProperty("not verifiable")
    not_verifiable_post,
    @JsonProperty("not credible")
    not_credible_post,
    @JsonProperty("mostly not credible")
    mostly_not_credible_post,
    @JsonProperty("credibility uncertain")
    credible_uncertain_post,
    @JsonProperty("mostly credible")
    mostly_credible_post,
    @JsonProperty("credible")
    credible_post;
}
