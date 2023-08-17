package com.example.proy001.domain;

import com.poiji.annotation.ExcelCellName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "pagodeluz")
public class Registro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    private String fecha;

    @NotNull
    private double lecturaMedidor;

    @NotNull
    private double consumoTotal;

    @NotNull
    @Column(name = "monto_recibo_soles")
    private double montoRecibo;

    @ExcelCellName("consumo_vecino")
    private double consumoVecino;

    @Column(name = "costo_watts_soles")
    private double costoWatts;


    @Column(name = "monto_vecino_soles")
    private double montoVecino;

    @Column(name = "gasto_luz_soles")
    private double gastoLuz;

}

