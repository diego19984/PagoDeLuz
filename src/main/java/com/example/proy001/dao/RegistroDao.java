package com.example.proy001.dao;

import com.example.proy001.domain.Registro;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RegistroDao extends CrudRepository<Registro, Long> {

    Optional<Registro> findByFecha(@NotEmpty String fecha);
}
