package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.records.EmergencyPatientInfo;
import edu.ncsu.csc.itrust2.repositories.DiagnosisRepository;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmergencyPatientService {
    private final DiagnosisRepository diagnosisRepository;
    private final OfficeVisitRepository officeVisitRepository;
    private final PatientService patientService;
    private final UserService userService;
    private final LoggerUtil loggerUtil;

    public EmergencyPatientInfo getPatientInformation(String patientName) {

        final Patient patient = (Patient) patientService.findByName(patientName);

        String currentUserName = loggerUtil.getCurrentUsername();
        User currentUser = userService.findByName(currentUserName);

        if (currentUser.isDoctor()) {
            loggerUtil.log(TransactionType.HCP_VIEW_ER, currentUserName, patientName);
        } else {
            loggerUtil.log(TransactionType.ER_VIEW_ER, currentUserName, patientName);
        }

        return new EmergencyPatientInfo(
                patient.getUsername(),
                patient.getFirstName(),
                patient.getPreferredName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getBloodType());
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

        return officeVisits.stream()
                .flatMap(officeVisit -> officeVisit.getPrescriptions().stream())
                .toList();
    }
}
