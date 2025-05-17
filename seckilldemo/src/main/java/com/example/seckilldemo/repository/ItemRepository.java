package com.example.seckilldemo.repository;

import com.example.seckilldemo.entity.Item;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 使用悲观锁读取数据（避免脏读）
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Optional<Item> findItemWithLock(@Param("id") Long id);
    @Modifying
    @Transactional
    @Query("UPDATE Item i SET i.stock = i.stock - 1, i.version = i.version + 1 WHERE i.id = :itemId AND i.version = :version ")
    int updateStockByIdWithVersion(@Param("itemId") Long itemId, @Param("version") Integer version);
}