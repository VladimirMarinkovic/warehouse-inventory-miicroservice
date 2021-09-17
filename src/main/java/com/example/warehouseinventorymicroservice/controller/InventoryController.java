package com.example.warehouseinventorymicroservice.controller;


import com.example.warehouseinventorymicroservice.dto.InventoryItemResponse;
import com.example.warehouseinventorymicroservice.dto.InventoryItemUpdateRequestDTO;
import com.example.warehouseinventorymicroservice.dto.InventoryItemsCountResponseDTO;
import com.example.warehouseinventorymicroservice.dto.InventoryPageResponseDTO;
import com.example.warehouseinventorymicroservice.service.InventoryService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequestMapping(path = "/inventory")
@Slf4j
@RestController
@RequiredArgsConstructor
public class InventoryController {


    private final InventoryService inventoryService;


    @GetMapping(value = "/{warehouseId}/count")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully returned inventory items count."),
            @ApiResponse(code = 400, message = "The received request is not syntactically valid. Correct the request and try again.")
    })
    ResponseEntity<InventoryItemsCountResponseDTO> getWarehouseInventoryItemsCount(@PathVariable("warehouseId") @NotBlank final String warehouseId) {
        log.debug("Getting Inventory items count for warehouseID: {}", warehouseId);
        return ResponseEntity.ok(this.inventoryService.getInventoryItemsCount(warehouseId));
    }


    @GetMapping(value = "/{warehouseId}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully returned inventory."),
            @ApiResponse(code = 400, message = "The received request is not syntactically valid. Correct the request and try again.")
    })
    ResponseEntity<InventoryPageResponseDTO> getWarehouseInventoryPage(@PathVariable("warehouseId") @NotBlank  final String warehouseId,
                                                                       @RequestParam(value = "pageNumber", required = false, defaultValue = "0") final Integer pageNumber,
                                                                       @RequestParam(value = "pageSize", required = false, defaultValue = "5") final Integer pageSize) {

        log.debug("Getting Inventory for warehouseID: {} with pageNumber: {} and pageSize: {}", warehouseId, pageNumber, pageSize);
        return ResponseEntity.ok(this.inventoryService.getWarehouseInventoryItemsPaged(warehouseId, pageNumber, pageSize));
    }

    @GetMapping(value = "/{warehouseId}/item/{itemId}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully returned inventory item."),
            @ApiResponse(code = 400, message = "The received request is not syntactically valid. Correct the request and try again.")
    })
    ResponseEntity<InventoryItemResponse> getWarehouseInventoryItem(@PathVariable("warehouseId") @NotBlank  final String warehouseId,
                                                                    @PathVariable("itemId") @NotBlank  final String itemId) {

        log.debug("Getting Inventory Item for warehouseID: {} and itemId {}", warehouseId, itemId);
        return ResponseEntity.ok(this.inventoryService.getInventoryItem(warehouseId, itemId));
    }

    @PutMapping(value = "/{warehouseId}/item/{itemId}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated inventory item."),
            @ApiResponse(code = 400, message = "The received request is not syntactically valid. Correct the request and try again.")
    })
    ResponseEntity<InventoryItemResponse> updateWarehouseInventoryItem(@PathVariable("warehouseId") @NotBlank  final String warehouseId,
                                                                       @PathVariable("itemId") @NotBlank  final String itemId,
                                                                       @RequestBody @Valid InventoryItemUpdateRequestDTO updateRequest) {

        log.debug("Updating Inventory Item for warehouseID: {} and itemId {}", warehouseId, itemId);
        return ResponseEntity.ok(this.inventoryService.updateInventoryItem(warehouseId, itemId, updateRequest));
    }
}
