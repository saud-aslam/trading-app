package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.view.PortfolioView;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import ca.jrvs.apps.trading.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(path = "/portfolio/traderId/{traderId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PortfolioView getPortfolio(@PathVariable Integer traderId) {
        try {
            return dashboardService.getProfileViewByTraderId(traderId);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @GetMapping(path = "profile/traderId/{traderId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TraderAccountView getProfile(@PathVariable Integer traderId) {
        try {
            return dashboardService.getTraderAccount(traderId);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}