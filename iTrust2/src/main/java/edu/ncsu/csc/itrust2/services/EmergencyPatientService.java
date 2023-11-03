package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.repositories.DiagnosisRepository;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmergencyPatientService {
    private final DiagnosisRepository diagnosisRepository;
    private final OfficeVisitRepository officeVisitRepository;
    private final PatientService patientService;

    public PatientInfo getPatientInformation(String patientName) {

        final Patient patient = (Patient) patientService.findByName(patientName);

        PatientInfo patientInfo =
                new PatientInfo(
                        patient.getFirstName(), patient.getPreferredName(),
                        patient.getLastName(), patient.getDateOfBirth(),
                        patient.getGender(), patient.getBloodType());
        return patientInfo;
    }

    public List<OfficeVisit> getRecentOfficeVisits(String patientName, int dateAmount) {
        final Patient patient = (Patient) patientService.findByName(patientName);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -dateAmount);
        Date startDate = calendar.getTime();
        Date endDate = new Date();

        Instant startDateInstant = startDate.toInstant();
        Instant endDateInstant = endDate.toInstant();
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        ZonedDateTime zoneStartDate = ZonedDateTime.ofInstant(startDateInstant, zoneId);
        ZonedDateTime zoneEndDate = ZonedDateTime.ofInstant(endDateInstant, zoneId);

        return officeVisitRepository.findByDateBetweenAndPatientOrderByDateDesc(
                zoneStartDate, zoneEndDate, patient);
    }

    public List<Diagnosis> getRecentDiagnoses(String patientName) {
        List<OfficeVisit> officeVisits = getRecentOfficeVisits(patientName, 60);

        List<Diagnosis> diagnoses = new ArrayList<>();
        for (OfficeVisit officeVisit : officeVisits) {
            List<Diagnosis> diagnosisList = diagnosisRepository.findByVisit(officeVisit);
            diagnoses.addAll(diagnosisList);
        }

        return diagnoses;
    }

    public List<Prescription> getRecentPrescriptions(String patientName) {
        List<OfficeVisit> officeVisits = getRecentOfficeVisits(patientName, 90);

        List<Prescription> prescriptions = new ArrayList<>();
        for (OfficeVisit officeVisit : officeVisits) {
            List<Prescription> diagnosisList = officeVisit.getPrescriptions();
            prescriptions.addAll(diagnosisList);
        }

        return prescriptions;
    }
}
