package com.batchprocessing.BatchProcessingExampleDemo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prodId;
    private String prodCode;
    private Double prodCost;
    private Double prodGst;
    private Double prodDisc;

    // Getters and setters...

    @Override
    public String toString() {
        return "Product{" +
                "prodId=" + prodId +
                ", prodCode='" + prodCode + '\'' +
                ", prodCost=" + prodCost +
                ", prodGst=" + prodGst +
                ", prodDisc=" + prodDisc +
                '}';
    }
}
