package com.test.purchasing.model.service;

import com.test.purchasing.model.entity.Good;
import com.test.purchasing.model.exception.GoodNotFoundException;
import com.test.purchasing.model.repository.GoodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoodService {
    private final GoodRepository goodRepository;

    public GoodService(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    public List<Good> findAll(){
        return goodRepository.findAll();
    }

    public Good findById(Long goodId) throws GoodNotFoundException {
        return goodRepository.findById(goodId).orElseThrow(()->new GoodNotFoundException(goodId));
    }
}
