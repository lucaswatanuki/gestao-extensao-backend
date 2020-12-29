package com.ftunicamp.tcc.controllers.request;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class RelatorioRequest {

    @Temporal(TemporalType.DATE)
    Date dataInicio;
}
