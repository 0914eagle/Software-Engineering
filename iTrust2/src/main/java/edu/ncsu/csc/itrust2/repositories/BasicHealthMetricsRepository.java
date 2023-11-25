package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicHealthMetricsRepository extends JpaRepository<BasicHealthMetrics, Long> {
    List<BasicHealthMetrics> findByPatient(User patient);
}
