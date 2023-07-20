package com.ecommerce.bikes;

import com.ecommerce.bikes.domain.Size;
import com.ecommerce.bikes.entity.SizeDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SizeMother {

    public static List<SizeDAO> sizesDAO = new ArrayList<>(Arrays.asList(
            new SizeDAO(1L, "M"),
            new SizeDAO(2L, "S")
    ));

    public static List<Size> sizes = new ArrayList<>(Arrays.asList(
            new Size(1L, "M", Collections.emptyList()),
            new Size(2L, "S", Collections.emptyList())
    ));
}
