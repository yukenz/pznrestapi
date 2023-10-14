package com.awan.pznrestapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {

    private Integer currentPage;
    private Integer toltalPage;
    private Integer sizePerPage;
    private Integer contentSize;

}
