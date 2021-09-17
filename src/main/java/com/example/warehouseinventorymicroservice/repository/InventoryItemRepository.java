package com.example.warehouseinventorymicroservice.repository;

import com.example.warehouseinventorymicroservice.model.InventoryItem;
import com.example.warehouseinventorymicroservice.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {

    Optional<InventoryItem> findByName(String name);

    Optional<InventoryItem> findByType(String type);

    Optional<InventoryItem> findById(UUID itemId);

    Optional<InventoryItem> findByWarehouseAndId(Warehouse warehouse, UUID itemId);

    List<InventoryItem> findAllByWarehouse(Warehouse warehouse);
}
