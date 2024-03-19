package com.viasoft.nfe.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ServiceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String state; // Estado ou autorizador
    private String authorization; // Autorização4
    private String returnAuthorization; // Retorno Autorização4
    private String inutilization; // Inutilização4
    private String protocolQuery; // Consulta Protocolo4
    private String serviceStatus; // Status Serviço4
    private String registrationQuery; // Consulta Cadastro4
    private String eventReception; // Recepção Evento4
    private LocalDate date; // Adicione esta linha


}
