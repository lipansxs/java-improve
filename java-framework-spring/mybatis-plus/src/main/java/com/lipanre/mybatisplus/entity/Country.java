package com.lipanre.mybatisplus.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author lipanre
 * @since 2023-03-02 00:37:14
 */

@Data
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private String continent;

    private String region;

    private BigDecimal surfaceArea;

    private Integer indepYear;

    private Integer population;

    private BigDecimal lifeExpectancy;

    private BigDecimal gnp;

    private BigDecimal gNPOld;

    private String localName;

    private String governmentForm;

    private String headOfState;

    private Integer capital;

    private String code2;
}
