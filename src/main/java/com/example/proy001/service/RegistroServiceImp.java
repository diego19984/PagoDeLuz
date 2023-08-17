package com.example.proy001.service;

import com.example.proy001.dao.RegistroDao;
import com.example.proy001.domain.Registro;

import com.example.proy001.dto.PagoLuzDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate fechaActual = LocalDate.parse(registro.getFecha(),formatter);
        LocalDate fechaAnterior = fechaActual.minusMonths(1);

        String fechaLecturaAnterior = fechaAnterior.format(formatter);

        Optional<Registro> registroOptional = registroDao.findByFecha(fechaLecturaAnterior);

        if(registroOptional.isPresent()){
            Registro registroAnterior=registroOptional.get();
            double lecturaMesAnterior = registroAnterior.getLecturaMedidor();
            double lecturaActual=registro.getLecturaMedidor();

            return Math.round((lecturaActual-lecturaMesAnterior)*100.0)/100.0;
        }
        return Math.round(registro.getLecturaMedidor()*100.0)/100.0;

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
        return Math.round((registro.getMontoRecibo()-registro.getMontoVecino())*100.0)/100.0;
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

    public void procesarExcel(String fileName){
        File file = new File(fileName);
        List<PagoLuzDto> pagosLuz = Poiji.fromExcel(file ,PagoLuzDto.class );
        List<Registro> registros = new ArrayList<>();

        DateTimeFormatter formatterEntrada= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterSalida= DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (PagoLuzDto pagoLuz:pagosLuz){

            Registro registro = new Registro();

            LocalDate fechaEntrada = LocalDate.parse(pagoLuz.getFecha(),formatterEntrada);
            String fechaSalida = fechaEntrada.format(formatterSalida);

            registro.setFecha(fechaSalida);
            registro.setLecturaMedidor(pagoLuz.getLecturaMedidor());
            registro.setConsumoTotal(pagoLuz.getConsumoTotal());
            registro.setMontoRecibo(pagoLuz.getMontoRecibo());
            registro.setConsumoVecino(pagoLuz.getConsumoVecino());
            registro.setCostoWatts(pagoLuz.getCostoWatts());
            registro.setMontoVecino(pagoLuz.getMontoVecino());
            registro.setGastoLuz(pagoLuz.getGastoLuz());

           registros.add(registro);
        }

        registroDao.saveAll(registros);
    }

}
