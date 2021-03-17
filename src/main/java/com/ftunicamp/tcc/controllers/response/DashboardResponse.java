package com.ftunicamp.tcc.controllers.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private int totalDocentes;
    private int autorizacoesPendentes;
    private int totalAtividades;
}
