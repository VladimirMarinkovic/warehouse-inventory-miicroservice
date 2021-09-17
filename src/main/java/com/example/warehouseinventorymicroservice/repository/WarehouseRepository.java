package com.example.warehouseinventorymicroservice.repository;

import com.example.warehouseinventorymicroservice.model.InventoryItem;
import com.example.warehouseinventorymicroservice.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {

    Optional<Warehouse> findById(UUID id);

    Optional<Warehouse> findByName(String name);
}
