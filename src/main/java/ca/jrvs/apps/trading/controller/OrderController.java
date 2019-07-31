
package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.dto.MarketOrderDto;
import ca.jrvs.apps.trading.model.dto.SecurityOrder;
import ca.jrvs.apps.trading.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(path = "/MarketOrder")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SecurityOrder putOrder(@RequestBody MarketOrderDto orderDto) {
        try {
            return orderService.executeMarketOrder(Collections.singletonList(orderDto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}





