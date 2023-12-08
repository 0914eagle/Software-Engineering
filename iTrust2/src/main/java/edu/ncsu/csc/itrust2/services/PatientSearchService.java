package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PatientSearchService {

    private final PatientRepository patientRepository;

    private boolean isSubsequenceOf(String string, String substring) {
        string = string.toLowerCase();
        substring = substring.toLowerCase();

        int subStringIndex = 0;
        int stringIndexT = 0;

        while (subStringIndex < substring.length() && stringIndexT < string.length()) {
            if (substring.charAt(subStringIndex) == string.charAt(stringIndexT)) {
                subStringIndex++;
                stringIndexT++;
            } else {
                stringIndexT++;
            }
        }
        return subStringIndex == substring.length();
    }

    public List<Patient> listByPatientName(String nameQuery) {
        // TODO: @Query(nativeQuery = true) (이름은조금 다를수도 있습니다) 를 레포지토리 메서드에 달아 SQL을 사용하여 쿼리 수준에서 해결하기
        return patientRepository.findAll().stream()
                .filter(
                        patient ->
                                isSubsequenceOf(
                                                patient.getFirstName()
                                                        + " "
                                                        + patient.getLastName(),
                                                nameQuery)
                                        || isSubsequenceOf(patient.getUsername(), nameQuery))
                .toList();
    }
}
