package edu.ncsu.csc.itrust2.controllers.routing;

import edu.ncsu.csc.itrust2.models.enums.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to manage basic abilities for ER roles
 *
 * @author Amalan Iyengar
 */
@Controller
@RequiredArgsConstructor
public class ERController {

    /**
     * Returns the ER for the given model
     *
     * @param model model to check
     * @return role
     */
    @RequestMapping(value = "er/index")
    @PreAuthorize("hasRole('ROLE_ER')")
    public String index(final Model model) {
        return Role.ROLE_ER.getLandingPage();
    }

    /**
     * Returns the ER for the given model
     *
     * @param model model to check
     * @return role
     */
    @RequestMapping(value = "er/records")
    @PreAuthorize("hasRole('ROLE_ER')")
    public String emergencyRecords(final Model model) {
        return "personnel/records";
    }

    /**
     * Returns the patient details page for the given model
     *
     * @param model model to check
     * @param username username of the patient
     * @return view for patient details
     */
    @RequestMapping(value = "er/patient_detail/{username}")
    @PreAuthorize("hasRole('ROLE_ER')")
    public String viewPatientDetail(final Model model, @PathVariable final String username) {
        // Add logic to fetch patient details based on the provided username
        // You may use a service to fetch the details from your data source

        // For example, you can pass the username to the model and let Thymeleaf
        // render the details in the patient_detail.html page
        model.addAttribute("username", username);

        return "er/patient_detail"; // Assuming the Thymeleaf template is in "er" directory
    }

    @RequestMapping(value = "er/search_er")
    @PreAuthorize("hasRole('ROLE_ER')")
    public String viewERPatients(final Model model) {
        return "er/search_er";
    }
}
