package com.viasoft.nfe.scraper;

import com.viasoft.nfe.model.ServiceStatus;
import com.viasoft.nfe.repository.ServiceStatusRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class NfeStatusScraper implements CommandLineRunner {

    private static final String STATUS_PAGE_URL = "http://www.nfe.fazenda.gov.br/portal/disponibilidade.aspx";

    @Autowired
    private ServiceStatusRepository repository;

    public List<ServiceStatus> fetchServiceStatuses() throws IOException {
        List<ServiceStatus> statuses = new ArrayList<>();
        Document doc = Jsoup.connect(STATUS_PAGE_URL).get();

        Elements rows = doc.select("#ctl00_ContentPlaceHolder1_gdvDisponibilidade2 tr:gt(0)");

        for (Element row : rows) {
            Elements cols = row.select("td");
            if (!cols.isEmpty()) {
                ServiceStatus status = new ServiceStatus();
                status.setState(cols.get(0).text()); // Estado ou autorizador
                status.setAuthorization(extractStatusFromImage(cols.get(1))); // Autorização4
                status.setReturnAuthorization(extractStatusFromImage(cols.get(2))); // Retorno Autorização4
                status.setInutilization(extractStatusFromImage(cols.get(3))); // Inutilização4
                status.setProtocolQuery(extractStatusFromImage(cols.get(4))); // Consulta Protocolo4
                status.setServiceStatus(extractStatusFromImage(cols.get(5))); // Status Serviço4
                // O campo "Tempo Médio" foi ignorado pois parece não ser um status
                status.setRegistrationQuery(extractStatusFromImage(cols.get(7))); // Consulta Cadastro4
                status.setEventReception(extractStatusFromImage(cols.get(8))); // Recepção Evento4
                status.setDate(LocalDate.now()); // Data de hoje

                statuses.add(status);
            }
        }

        return statuses;
    }

    // Método auxiliar para extrair o status do serviço baseado na imagem
    private String extractStatusFromImage(Element element) {
        // Extrai o nome do arquivo da imagem
        String src = element.select("img").attr("src");
        if (src.contains("bola_verde")) {
            return "Verde - Resposta positiva."; // Operacional
        } else if (src.contains("bola_amarela")) {
            return "Amarela - a consulta retornou a primeira resposta negativa (falta Serviço ou falha de conexão)."; // Alerta
        } else if (src.contains("bola_vermelha")) {
            return "Vermelha - há respostas negativas seguidas para uma consulta (falta Serviço ou falha de conexão)."; // Problema
        } else {
            return "Indisponível"; // Não disponível ou desconhecido
        }
    }

    public void fetchAndStoreServiceStatuses() throws IOException {
        List<ServiceStatus> fetchedStatuses = fetchServiceStatuses();
        LocalDate currentDate = LocalDate.now();

        for (ServiceStatus fetchedStatus : fetchedStatuses) {
            // Tenta encontrar um registro existente para o estado e a data
            Optional<ServiceStatus> existingStatusOpt = repository.findByStateAndDate(fetchedStatus.getState(), currentDate)
                    .stream()
                    .findFirst();

            if (existingStatusOpt.isPresent()) {
                // Se um registro existir, exclua-o
                repository.delete(existingStatusOpt.get());
            }

            // Seja um registro novo ou depois de deletar o existente, cria e salva um novo
            fetchedStatus.setDate(currentDate);
            repository.save(fetchedStatus);
        }
    }



    @Override
    public void run(String... args) throws IOException {
        fetchAndStoreServiceStatuses();
    }
}
