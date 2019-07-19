package ca.jrvs.apps.trading.model.dto;

public interface Entity<ID> {
    ID getId();
    void setId(ID id);
}
