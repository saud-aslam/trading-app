package ca.jrvs.apps.trading.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ticker",
        "position",
        "quote"
})
public class SecurityRows {

    @JsonProperty("ticker")
    private String ticker;
    @JsonProperty("position")
    private Position position;
    @JsonProperty("quote")
    private Quote quote;

    @JsonProperty("ticker")
    public String getTicker() {
        return ticker;
    }

    @JsonProperty("ticker")
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @JsonProperty("position")
    public Position getPosition() {
        return position;
    }

    @JsonProperty("position")
    public void setPosition(Position position) {
        this.position = position;
    }

    @JsonProperty("quote")
    public Quote getQuote() {
        return quote;
    }

    @JsonProperty("quote")
    public void setQuote(Quote quote) {
        this.quote = quote;
    }

}