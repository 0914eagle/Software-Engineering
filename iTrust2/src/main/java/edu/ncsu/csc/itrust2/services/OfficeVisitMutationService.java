package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.*;
import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
@RequiredArgsConstructor
public class OfficeVisitMutationService {

    private final OfficeVisitRepository officeVisitRepository;

    private final UserService userService;

    private final AppointmentRequestService appointmentRequestService;

    private final HospitalService hospitalService;

    private final BasicHealthMetricsService bhmService;

    private final PrescriptionService prescriptionService;

    private final DiagnosisService diagnosisService;

    private final OphthalmologySurgeryService ophthalmologySurgeryService;

    public OfficeVisit create(final OfficeVisitForm ovf) {
        final OfficeVisit ov = new OfficeVisit();

        ov.setId(assertNotExistsById(ovf.getId()));

        ov.setPatient(userService.findByName(ovf.getPatient()));
        ov.setHcp(userService.findByName(ovf.getHcp()));
        ov.setNotes(ovf.getNotes());
        ov.setDate(ZonedDateTime.parse(ovf.getDate()));
        ov.setAppointment(getAppointmentRequest(ov, ovf.getPreScheduled()));
        ov.setHospital(hospitalService.findByName(ovf.getHospital()));
        ov.setBasicHealthMetrics(getBasicHealthMetrics(ovf));
        ov.setDiagnoses(getDiagnoses(ovf.getDiagnoses()));
        ov.setPrescriptions(getPrescriptions(ovf.getPrescriptions()));

        ov.setType(AppointmentType.parseOrDefault(ovf.getType()));

        return officeVisitRepository.save(ov);
    }

    public OfficeVisit update(final OfficeVisitForm ovf) {
        final OfficeVisit ov = new OfficeVisit();

        ov.setId(assertExistsById(ovf.getId()));

        ov.setPatient(userService.findByName(ovf.getPatient()));
        ov.setHcp(userService.findByName(ovf.getHcp()));
        ov.setNotes(ovf.getNotes());
        ov.setDate(ZonedDateTime.parse(ovf.getDate()));
        ov.setAppointment(getAppointmentRequest(ov, ovf.getPreScheduled()));
        ov.setHospital(hospitalService.findByName(ovf.getHospital()));
        ov.setBasicHealthMetrics(getBasicHealthMetrics(ovf));
        ov.setDiagnoses(getDiagnoses(ovf.getDiagnoses()));
        ov.setPrescriptions(getPrescriptions(ovf.getPrescriptions()));

        ov.setType(AppointmentType.parseOrDefault(ovf.getType()));

        return officeVisitRepository.save(ov);
    }

    public OfficeVisit createForOphthalmologySurgery(OphthalmologySurgeryForm osf) {
        final OfficeVisit ov = new OfficeVisit();

        ov.setId(assertNotExistsById(osf.getId()));

        ov.setPatient(userService.findByName(osf.getPatient()));
        ov.setHcp(userService.findByName(osf.getHcp()));
        ov.setNotes(osf.getNotes());
        ov.setDate(ZonedDateTime.parse(osf.getDate()));
        ov.setAppointment(getAppointmentRequest(ov, osf.getPreScheduled()));
        ov.setHospital(hospitalService.findByName(osf.getHospital()));
        ov.setBasicHealthMetrics(getBasicHealthMetrics(osf));
        ov.setDiagnoses(getDiagnoses(osf.getDiagnoses()));
        ov.setPrescriptions(getPrescriptions(osf.getPrescriptions()));

        ov.setType(AppointmentType.OPHTHALMOLOGY_SURGERY);
        ov.setOphthalmologySurgery(getOphthalmologySurgery(osf));

        return officeVisitRepository.save(ov);
    }

    public OfficeVisit updateForOphthalmologySurgery(
            Long id, UpdateOfficeVisitForm officeVisitForm) {

        Optional<OfficeVisit> ovOptional = officeVisitRepository.findById(id);
        if (ovOptional.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Office visit with the id " + id + " doesn't exist");
        }

        final OfficeVisit ov = ovOptional.get();
        ov.setDate(ZonedDateTime.parse(officeVisitForm.getDate()));
        ov.setOphthalmologySurgery(
                ophthalmologySurgeryService.update(
                        ov.getOphthalmologySurgery().getId(),
                        officeVisitForm.getOphthalmologySurgery()));
        ov.setNotes(officeVisitForm.getNotes());

        return officeVisitRepository.save(ov);
    }

    public Long assertNotExistsById(String idString) {
        if (idString == null) return null;

        final var id = Long.parseLong(idString);
        if (officeVisitRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Office visit with the id " + id + " already exists");
        }

        return id;
    }

    public Long assertExistsById(String idString) {
        if (idString == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is null");

        final var id = Long.parseLong(idString);
        if (!officeVisitRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Office visit with the id " + id + " doesn't exist");
        }

        return id;
    }

    public AppointmentRequest getAppointmentRequest(
            final OfficeVisit ov, final String preScheduled) {
        AppointmentRequest ar = null;
        if (preScheduled != null) {
            final var request =
                    appointmentRequestService.findByHcpAndPatientAndDate(
                            ov.getHcp(), ov.getPatient(), ov.getDate());

            request.orElseThrow(
                    () ->
                            new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "Marked as pre-schedule but no match can be found"));

            ar = request.get();
        }

        return ar;
    }

    public BasicHealthMetrics getBasicHealthMetrics(final OfficeVisitForm ovf) {
        return bhmService.build(ovf);
    }

    public List<Diagnosis> getDiagnoses(final List<DiagnosisForm> diagnosisForms) {
        List<Diagnosis> diagnoses = null;
        if (diagnosisForms != null) {
            diagnoses =
                    diagnosisForms.stream()
                            .map(diagnosisService::build)
                            .collect(Collectors.toList());
        }

        return diagnoses;
    }

    public List<Prescription> getPrescriptions(final List<PrescriptionForm> prescriptionForms) {
        List<Prescription> prescriptions = null;
        if (prescriptionForms != null) {
            prescriptions =
                    prescriptionForms.stream()
                            .map(prescriptionService::build)
                            .collect(Collectors.toList());
        }

        return prescriptions;
    }

    public OphthalmologySurgery getOphthalmologySurgery(final OphthalmologySurgeryForm osf) {
        return ophthalmologySurgeryService.create(osf);
    }
}
