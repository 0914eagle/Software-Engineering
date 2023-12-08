package edu.ncsu.csc.itrust2.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOfficeVisitForm {
    private String date;
    private String id;
    private String notes;
    private UpdateOphthalmologySurgeryForm ophthalmologySurgery;
}
