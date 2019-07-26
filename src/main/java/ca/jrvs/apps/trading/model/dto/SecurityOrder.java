package ca.jrvs.apps.trading.model.dto;

public class SecurityOrder implements Entity<Integer> {
    private Integer id;
    private Integer accountId;
    private orderStatus status;
    private Integer size;
    private Double price;
    private String notes;
    private String ticker;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer s) {
        this.id = s;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public orderStatus getStatus() {
        return status;
    }

    public void setStatus(orderStatus status) {
        this.status = status;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public String toString() {
        return "SecurityOrder{" +
                ", accountId=" + accountId +
                ", status='" + status + '\'' +
                ", size=" + size +
                ", price=" + price +
                ", notes='" + notes + '\'' +
                '}';
    }

    public enum orderStatus {
        FILLED, CANCELLED, PENDING
    }
}