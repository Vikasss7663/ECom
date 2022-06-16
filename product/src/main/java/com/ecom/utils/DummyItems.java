package com.ecom.utils;

import com.ecom.domain.Product;

import java.time.LocalDate;
import java.util.Date;

public class DummyItems {

    private DummyItems() {}

    public static Product[] getDummyProducts() {

        return new Product[] {new Product("1", "Default Product-1", "Desc",  12000d, "", 16, "1", LocalDate.now(), LocalDate.now()),
                    new Product("2", "Default Product-2", "Desc",  12000d, "", 22, "1", LocalDate.now(), LocalDate.now())
        };
    }

    public static Product getDummyProduct() {

        return new Product("3", "Default Product-3", "Desc",  12000d, "", 28, "1", LocalDate.now(), LocalDate.now());
    }
}
