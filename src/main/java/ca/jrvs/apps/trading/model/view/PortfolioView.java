package ca.jrvs.apps.trading.model.view;

import ca.jrvs.apps.trading.model.dto.SecurityRows;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "securityRows"
})
public class PortfolioView {

    @JsonProperty("securityRows")
    private List<SecurityRows> securityRows = null;

    @JsonProperty("securityRows")
    public List<SecurityRows> getSecurityRows() {
        return securityRows;
    }

    @JsonProperty("securityRows")
    public void setSecurityRows(List<SecurityRows> securityRows) {
        this.securityRows = securityRows;
    }

}