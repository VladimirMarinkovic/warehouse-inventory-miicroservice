package com.example.warehouseinventorymicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Table(name = "inventory_item")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem implements Serializable {

    private static final long serialVersionUID = -7703750955400959507L;

    @Id
    @Column(name = "id", columnDefinition = "char(36)")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    private UUID id;


    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "type", nullable = false)
    private String type;

    @OneToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;




}
