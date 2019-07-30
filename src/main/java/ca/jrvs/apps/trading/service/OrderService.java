package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.dto.MarketOrderDto;
import ca.jrvs.apps.trading.model.dto.SecurityOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private AccountDao accountDao;
    private SecurityOrderDao securityOrderDao;
    private QuoteDao quoteDao;
    private PositionDao positionDao;

    @Autowired
    public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao,
                        QuoteDao quoteDao, PositionDao positionDao) {
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderDao;
        this.quoteDao = quoteDao;
        this.positionDao = positionDao;
    }

    /**
     * Execute a market order
     * <p>
     * - validate the order (e.g. size, and ticker)
     * - Create a securityOrder (for security_order table)
     * - Handle buy or sell order
     * - buy order : check account balance
     * - sell order: check position for the ticker/symbol
     * - (please don't forget to update securityOrder.status)
     * - Save and return securityOrder
     * <p>
     * NOTE: you will need to some helper methods (protected or private)
     *
     * @param orderDtos market order
     * @return SecurityOrder from security_order table
     * @throws org.springframework.dao.DataAccessException if unable to get data from DAO
     * @throws IllegalArgumentException                    for invalid input
     */
    public SecurityOrder executeMarketOrder(List<MarketOrderDto> orderDtos) {
        Double askPrice = 0.0;
        Double amount = 0.0;
        SecurityOrder securityOrder = new SecurityOrder();
        for (MarketOrderDto orderDto : orderDtos) {
            if (orderDto.getSize() <= 0 || orderDto.getTicker() == null) {
                throw new IllegalArgumentException("Invalid input");
            }

            securityOrder.setAccountId(orderDto.getAccountId());
            securityOrder.setSize(orderDto.getSize());
            securityOrder.setTicker(orderDto.getTicker());
            try {
                askPrice = (quoteDao.findById(orderDto.getTicker())).getAskPrice();
                amount = (accountDao.findById(orderDto.getAccountId())).getAmount();
            } catch (DataAccessException e) {
                e.getMessage();
            }

            if (amount < (orderDto.getSize() * askPrice)) {
                securityOrder.setStatus(SecurityOrder.orderStatus.CANCELLED);
                securityOrder.setNotes("You have less money in account, minimum should be " + orderDto.getSize() * askPrice);
            } else {
                securityOrder.setStatus(SecurityOrder.orderStatus.FILLED);
                securityOrder.setNotes(null);

            }

        }

        return securityOrderDao.save(securityOrder);
    }

}
