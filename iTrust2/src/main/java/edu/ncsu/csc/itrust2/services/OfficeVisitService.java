package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class OfficeVisitService extends Service {

    private final OfficeVisitRepository officeVisitRepository;

    @Override
    protected JpaRepository getRepository() {
        return officeVisitRepository;
    }

    public List<OfficeVisit> findByHcp(final User hcp) {
        return officeVisitRepository.findByHcp(hcp);
    }

    public List<OfficeVisit> findByPatient(final User patient) {
        return officeVisitRepository.findByPatient(patient);
    }

    public List<OfficeVisit> findByHcpAndPatient(final User hcp, final User patient) {
        return officeVisitRepository.findByHcpAndPatient(hcp, patient);
    }
}
