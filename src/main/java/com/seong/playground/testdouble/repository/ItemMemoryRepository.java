package com.seong.playground.testdouble.repository;

import com.seong.playground.testdouble.service.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemMemoryRepository implements ItemRepository {

}
