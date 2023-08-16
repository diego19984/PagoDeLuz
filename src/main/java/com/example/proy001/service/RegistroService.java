package com.example.proy001.service;

import com.example.proy001.domain.Registro;

import java.util.List;

public interface RegistroService {

    public List<Registro> listarRegistros();

    public void guardar(Registro registro);

    public void eliminar(Registro registro);

    public Registro encontrarRegistro(Registro registro);

}
