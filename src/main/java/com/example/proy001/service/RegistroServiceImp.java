package com.example.proy001.service;

import com.example.proy001.dao.RegistroDao;
import com.example.proy001.domain.Registro;

import com.poiji.bind.Poiji;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.nio.file.Paths.get;


@Service
public class RegistroServiceImp implements RegistroService {

    @Autowired
    private RegistroDao registroDao;


    @Override
    @Transactional(readOnly = true)
    public List<Registro> listarRegistros() {
        var registros = (List<Registro>) registroDao.findAll();
        return registros;
    }

    @Override
    @Transactional
    public void guardar(Registro registro) {
        registro.setConsumoVecino(calcularConsumoVecino(registro));
        registro.setCostoWatts(calcularCostoWatts(registro));
        registro.setMontoVecino(calcularMontoVecino(registro));
        registro.setGastoLuz(calcularGastoLuz(registro));
        registroDao.save(registro);
    }

    @Override
    @Transactional
    public void eliminar(Registro registro) {
        registroDao.delete(registro);
    }

    @Override
    @Transactional(readOnly = true)
    public Registro encontrarRegistro(Registro registro) {
        return registroDao.findById(registro.getId()).orElse(null);
    }

    public double calcularConsumoVecino(Registro registro){

        LocalDate fechaActual=registro.getFechaAsLocalDate();
        LocalDate fechaAnterior=fechaActual.minusMonths(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String fechaLecturaAnterior=fechaAnterior.format(formatter);

        Optional<Registro> registroOptional=registroDao.findByFecha(fechaLecturaAnterior);

        if(registroOptional.isPresent()){
            Registro registroAnterior=registroOptional.get();
            double lecturaMesAnterior = registroAnterior.getLecturaMedidor();
            double lecturaActual=registro.getLecturaMedidor();

            return lecturaActual-lecturaMesAnterior;
        }
        return registro.getLecturaMedidor();

    }

    public double calcularCostoWatts(Registro registro){

        double costo_x_watts=registro.getMontoRecibo()/registro.getConsumoTotal();

        return Math.round(costo_x_watts*100.0)/100.0;
    }

    public double calcularMontoVecino(Registro registro){

        double montoVecino=registro.getConsumoVecino()*registro.getCostoWatts()*16/15;
        return Math.round(montoVecino*100.0)/100.0;
    }

    public double calcularGastoLuz(Registro registro){
        return registro.getMontoRecibo()-registro.getMontoVecino();
    }


    public String UploadFile(@RequestParam("file") MultipartFile file){
        try {
            byte[] fileBytes = file.getBytes();

            String fileName=file.getOriginalFilename();

            String dirUploads = System.getProperty("user.dir")+"/uploads";
            String filePath = dirUploads+"/"+fileName;
            Path uploadPath = Paths.get(dirUploads);
            Path builderPath = Paths.get(filePath);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            Files.write(builderPath,fileBytes);

            return filePath;
        } catch (IOException e) {

            return "Error";
        }

    }

    public void listarRegistrosExcel(String fileName){

        File file = new File(fileName);
        List<Registro> registros = Poiji.fromExcel(file ,Registro.class );
        registroDao.saveAll(registros);
    }

}
