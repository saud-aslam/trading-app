package ca.jrvs.apps.trading.model.dto;

public class Position implements Entity<Integer> {
    private Integer accountId;
    private Integer position;
    private String ticker;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public Integer getId() {
        return accountId;
    }

    @Override
    public void setId(Integer s) {
        this.accountId = s;
    }

    @Override
    public String toString() {
        return "Position{" +
                "accountId=" + accountId +
                ", position=" + position +
                ", ticker='" + ticker + '\'' +
                '}';
    }
}