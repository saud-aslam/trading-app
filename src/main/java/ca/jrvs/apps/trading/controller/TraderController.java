
package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.dto.Account;
import ca.jrvs.apps.trading.model.dto.Trader;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import ca.jrvs.apps.trading.service.FundTransferService;
import ca.jrvs.apps.trading.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/trader")
public class TraderController {

    private RegisterService registerService;
    private TraderDao traderDao;
    private FundTransferService fundTransferService;

    @Autowired
    public TraderController(RegisterService registerService, TraderDao traderDao,
                            FundTransferService fundTransferService) {
        this.registerService = registerService;
        this.traderDao = traderDao;
        this.fundTransferService = fundTransferService;
    }

    @DeleteMapping(path = "/traderId/{traderId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTrader(@PathVariable Integer traderId) {
        try {
            registerService.deleteTraderById(traderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TraderAccountView putTraderandAccount(@RequestBody Trader trader) {
        try {
            return registerService.createTraderAndAccount(trader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(path = "/trader/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TraderAccountView createTraderandAccount(@PathVariable String firstname,
                                                    @PathVariable String lastname,
                                                    @PathVariable String dob,
                                                    @PathVariable String country,
                                                    @PathVariable String email) {
        try {
            Trader trader = new Trader();
            trader.setFirstName(firstname);
            trader.setLastName(lastname);
            trader.setDob(LocalDate.parse(dob));
            trader.setCountry(country);
            trader.setEmail(email);
            return registerService.createTraderAndAccount(trader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(path = "/deposit/traderId/{traderId}/amount/{amount}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Account deposit(@PathVariable Integer traderId, @PathVariable Double amount) {
        try {
            return fundTransferService.deposit(traderId, amount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(path = "/withdraw/traderId/{traderId}/amount/{amount}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Account withdraw(@PathVariable Integer traderId, @PathVariable Double amount) {
        try {
            return fundTransferService.withdraw(traderId, amount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}





