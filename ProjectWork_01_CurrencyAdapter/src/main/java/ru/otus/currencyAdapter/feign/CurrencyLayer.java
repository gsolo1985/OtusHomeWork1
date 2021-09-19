package ru.otus.currencyAdapter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(value = "currencyLayer", url = "http://api.currencylayer.com/")
public interface CurrencyLayer {

    @RequestMapping("historical?access_key=${currencyLayer.access_key}&date={date}")
    public Map<String, Object> getRatesByDate(@PathVariable("date") String date);
}
