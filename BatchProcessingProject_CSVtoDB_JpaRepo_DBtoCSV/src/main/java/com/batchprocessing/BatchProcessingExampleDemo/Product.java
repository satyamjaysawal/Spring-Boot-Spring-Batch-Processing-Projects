package com.batchprocessing.BatchProcessingExampleDemo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "prodId")
    private Integer prodId;

//    @Column(name = "prodCode")
    private String prodCode;

//    @Column(name = "prodCost")
    private Double prodCost;

//    @Column(name = "prodGst")
    private Double prodGst;

//    @Column(name = "prodDisc")
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
