package com.example.proy001.service;

import com.example.proy001.domain.Registro;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RegistroService {

    List<Registro> listarRegistros();

    void guardar(Registro registro);

    void eliminar(Registro registro);

    Registro encontrarRegistro(Registro registro);

    double calcularConsumoVecino(Registro registro);

    double calcularCostoWatts(Registro registro);

    double calcularMontoVecino(Registro registro);

    double calcularGastoLuz(Registro registro);

    String UploadFile(@RequestParam("file") MultipartFile file);

    void procesarExcel(String fileName);



}
