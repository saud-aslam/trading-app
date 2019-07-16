package ca.jrvs.apps.trading.model.dto;

public interface Entity<ID> {
    ID getID();
    void setId(ID id);
}
