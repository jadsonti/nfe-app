package com.viasoft.nfe.controller;

import com.viasoft.nfe.model.ServiceStatus;
import com.viasoft.nfe.repository.ServiceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/status")
public class ServiceStatusController {

    @Autowired
    private ServiceStatusRepository repository;

    @GetMapping("/current")
    public List<ServiceStatus> getCurrentServiceStatuses() {
        return repository.findAll();
    }

    @GetMapping("/current/{state}")
    public List<ServiceStatus> getCurrentServiceStatusByState(@PathVariable String state) {
        return repository.findByState(state);
    }

    @GetMapping("/{state}/byDate/{date}")
    public List<ServiceStatus> getStatusByStateAndDate(
            @PathVariable String state,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return repository.findByStateAndDate(state, date);
    }

    @GetMapping("/byDate/{date}")
    public List<ServiceStatus> getServiceStatusByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return repository.findByDate(date);
    }


    @GetMapping("/mostUnavailable")
    public String getMostUnavailableStates() {
        List<Object[]> results = repository.countIndisponibilitiesByState();
        if (!results.isEmpty()) {
            // Descobre o maior número de indisponibilidades
            long maxIndisponibilities = (long) results.get(0)[1]; // Assumindo que este é o máximo inicial

            // Filtra todos os estados que têm o mesmo número máximo de indisponibilidades
            List<String> maxStates = results.stream()
                    .filter(result -> (long) result[1] == maxIndisponibilities)
                    .map(result -> (String) result[0])
                    .collect(Collectors.toList());

            // Verifica se há mais de um estado com o máximo de indisponibilidades
            if (maxStates.size() > 1) {
                // Mais de um estado tem o máximo de indisponibilidades
                String statesStr = String.join(", ", maxStates);
                return "Os estados com mais indisponibilidades são: " + statesStr + " com " + maxIndisponibilities + " indisponibilidades cada um.";
            } else {
                // Apenas um estado tem o máximo de indisponibilidades
                return "O estado com mais indisponibilidades é: " + maxStates.get(0) + " com " + maxIndisponibilities + " indisponibilidades.";
            }
        }
        return "Não foram encontradas indisponibilidades.";
    }


}

