package edu.ncsu.csc.itrust2.controllers.routing;

import edu.ncsu.csc.itrust2.models.enums.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class responsible for managing the behavior for the HCP Landing Screen
 *
 * @author Kai Presler-Marshall
 */
@Controller
@RequiredArgsConstructor
public class HCPController {

    /**
     * Returns the Landing screen for the HCP
     *
     * @param model Data from the front end
     * @return The page to display
     */
    @RequestMapping(value = "hcp/index")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String index(final Model model) {
        return Role.ROLE_HCP.getLandingPage();
    }

    /**
     * Returns the page allowing HCPs to edit patient demographics
     *
     * @return The page to display
     */
    @GetMapping("/hcp/editPatientDemographics")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String editPatientDemographics() {
        return "hcp/editPatientDemographics";
    }

    /**
     * Returns the page allowing HCPs to edit prescriptions
     *
     * @return The page to display
     */
    @GetMapping("/hcp/editPrescriptions")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String editPrescriptions() {
        return "hcp/editPrescriptions";
    }

    /**
     * Returns the ER for the given model
     *
     * @param model model to check
     * @return role
     */
    @RequestMapping(value = "hcp/records")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String emergencyRecords(final Model model) {
        return "personnel/records";
    }

    /**
     * Method responsible for HCP's Accept/Reject requested appointment functionality. This prepares
     * the page.
     *
     * @param model Data for the front end
     * @return The page to display to the user
     */
    @GetMapping("/hcp/appointmentRequests")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String requestAppointmentForm(final Model model) {
        return "hcp/appointmentRequests";
    }

    /**
     * Returns the form page for a HCP to document an OfficeVisit
     *
     * @param model The data for the front end
     * @return Page to display to the user
     */
    @GetMapping("/hcp/documentOfficeVisit")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String documentOfficeVisit(final Model model) {
        return "hcp/documentOfficeVisit";
    }

    @GetMapping("/hcp/foodDiary")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String viewPatientFoodDiary(final Model model) {
        return "hcp/foodDiary";
    }

    @GetMapping("/hcp/search_hcp")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String viewHCPPatients(final Model model) {
        return "hcp/search_hcp";
    }

    @RequestMapping(value = "/hcp/hcp_details/{username}")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String viewPatientDetail(final Model model, @PathVariable final String username) {
        // Add logic to fetch patient details based on the provided username
        // You may use a service to fetch the details from your data source

        // For example, you can pass the username to the model and let Thymeleaf
        // render the details in the patient_detail.html page
        model.addAttribute("username", username);

        return "hcp/hcp_details"; // Assuming the Thymeleaf template is in "er" directory
    }

    @GetMapping("/hcp/personalRepresentatives")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String viewPersonalRepresentatives(final Model model) {
        return "hcp/personalRepresentatives";
    }

    @GetMapping("/hcp/editOfficeVisit")
    @PreAuthorize("hasRole('ROLE_OPH')")
    public String viewEditOfficeVisit(final Model model) {
        return "hcp/editOfficeVisit";
    }
}
