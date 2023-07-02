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
public class Countrylanguage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String countryCode;

    private String language;

    private String isOfficial;

    private BigDecimal percentage;
}
