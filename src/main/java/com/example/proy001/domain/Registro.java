package com.example.proy001.domain;


import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelSheet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "pagodeluz")
@ExcelSheet("pagodeluz_db")
public class Registro implements Serializable {

    @Id
    @ExcelCellName("id")
    private long id;

    @NotEmpty
    @ExcelCellName("fecha")
    private String fecha;

    @NotNull
    @ExcelCellName("lectura_medidor")
    private double lecturaMedidor;

    @NotNull
    @ExcelCellName("consumo_total")
    private double consumoTotal;

    @NotNull
    @ExcelCellName("monto_recibo_soles")
    @Column(name = "monto_recibo_soles")
    private double montoRecibo;

    @ExcelCellName("consumo_vecino")
    private double consumoVecino;

    @Column(name = "costo_watts_soles")
    @ExcelCellName("costo_watts_soles")
    private double costoWatts;


    @Column(name = "monto_vecino_soles")
    @ExcelCellName("monto_vecino_soles")
    private double montoVecino;

    @Column(name = "gasto_luz_soles")
    @ExcelCellName("gasto_luz_soles")
    private double gastoLuz;

    public LocalDate getFechaAsLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(fecha, formatter);
    }


}

