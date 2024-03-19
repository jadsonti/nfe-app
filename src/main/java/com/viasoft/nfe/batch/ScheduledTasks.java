package com.viasoft.nfe.batch;

import com.viasoft.nfe.scraper.NfeStatusScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScheduledTasks {
    @Autowired
    private NfeStatusScraper scraper;

    @Scheduled(fixedRate = 300000) // 300000 ms = 5 minutos
    public void reportCurrentTime() throws IOException {
        scraper.fetchAndStoreServiceStatuses();
    }
}
