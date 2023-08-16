package com.example.proy001.service;

import com.example.proy001.domain.Registro;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RegistroService {

    public List<Registro> listarRegistros();

    public void guardar(Registro registro);

    public void eliminar(Registro registro);

    public Registro encontrarRegistro(Registro registro);

    public double calcularConsumoVecino(Registro registro);

    public double calcularCostoWatts(Registro registro);

    public double calcularMontoVecino(Registro registro);

    public double calcularGastoLuz(Registro registro);

    public String UploadFile(@RequestParam("file") MultipartFile file);

    public void listarRegistrosExcel(String fileName);



}
