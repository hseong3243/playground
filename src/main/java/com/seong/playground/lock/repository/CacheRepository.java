package com.seong.playground.lock.repository;

public interface CacheRepository {

    Boolean lock(Long key);

    Boolean unlock(Long key);
}
