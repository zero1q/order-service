/*
 * Created by zeeroiq on 9/16/20, 2:46 AM
 */

package com.shri.orderservice.mappers;

import com.shri.orderservice.domain.BeerOrderLine;
import com.shri.model.BeerDto;
import com.shri.model.BeerOrderLineDto;
import com.shri.orderservice.services.beer.BeerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
@Slf4j
public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper{

    private BeerService beerService;
    private BeerOrderLineMapper orderLineMapper;

    @Autowired
    public void setBeerService(BeerService beerService) {
        this.beerService = beerService;
    }

    @Autowired
    public void setOrderLineMapper(BeerOrderLineMapper orderLineMapper) {
        this.orderLineMapper = orderLineMapper;
    }

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine orderLine) {
        BeerOrderLineDto beerOrderLineDto = orderLineMapper.beerOrderLineToDto(orderLine);
        Optional<BeerDto> beerByUpc = beerService.getBeerByUpc(orderLine.getUpc());

        beerByUpc.ifPresentOrElse(beerDto -> {
            beerOrderLineDto.setName(beerDto.getBeerName());
            beerOrderLineDto.setBeerStyle(beerDto.getBeerStyle());
            beerOrderLineDto.setUpc(beerDto.getUpc());
            beerOrderLineDto.setPrice(beerDto.getPrice());
            beerOrderLineDto.setBeerId(beerDto.getId());
        }, () -> log.error("Beer not found with UPC: " + beerByUpc.get()));

        return beerOrderLineDto;
    }
}
