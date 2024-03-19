package com.viasoft.nfe.repository;

import com.viasoft.nfe.model.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServiceStatusRepository extends JpaRepository<ServiceStatus, Long> {
    List<ServiceStatus> findByState(String state);
    List<ServiceStatus> findByDate(LocalDate date);
    List<ServiceStatus> findByStateAndDate(String state, LocalDate date);

    @Query("SELECT s.state, COUNT(s) FROM ServiceStatus s WHERE " +
            "(s.authorization = 'Indisponível' OR s.returnAuthorization = 'Indisponível' OR " +
            "s.inutilization = 'Indisponível' OR s.protocolQuery = 'Indisponível' OR " +
            "s.serviceStatus = 'Indisponível' OR s.registrationQuery = 'Indisponível' OR " +
            "s.eventReception = 'Indisponível') " +
            "GROUP BY s.state ORDER BY COUNT(s) DESC")
    List<Object[]> countIndisponibilitiesByState();



}
