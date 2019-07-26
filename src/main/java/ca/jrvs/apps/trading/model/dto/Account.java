package ca.jrvs.apps.trading.model.dto;

public class Account implements Entity<Integer> {
    private Integer id;
    private Integer traderId;
    private Double amount;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer s) {
        this.id = s;
    }

    public Integer getTraderId() {
        return traderId;
    }

    public void setTraderId(Integer traderId) {
        this.traderId = traderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", traderId=" + traderId +
                ", amount=" + amount +
                '}';
    }
}
