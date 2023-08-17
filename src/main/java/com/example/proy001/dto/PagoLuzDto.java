package com.example.proy001.dto;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelSheet;

import lombok.Data;

import java.io.Serializable;

@Data
@ExcelSheet("pagodeluz_db")
public class PagoLuzDto implements Serializable {

    @ExcelCellName("fecha")
    private String fecha;

    @ExcelCellName("lectura_medidor")
    private double lecturaMedidor;

    @ExcelCellName("consumo_total")
    private double consumoTotal;


    @ExcelCellName("monto_recibo_soles")

    private double montoRecibo;

    @ExcelCellName("consumo_vecino")
    private double consumoVecino;

    @ExcelCellName("costo_watts_soles")
    private double costoWatts;


    @ExcelCellName("monto_vecino_soles")
    private double montoVecino;

    @ExcelCellName("gasto_luz_soles")
    private double gastoLuz;
}
