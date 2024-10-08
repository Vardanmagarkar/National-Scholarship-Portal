package com.nsp.backend.Controller;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nsp.backend.Repository.CombinedFormRepository;
import com.nsp.backend.Repository.NtseFormRepository;
import com.nsp.backend.model.CombinedForm;
import com.nsp.backend.model.InstituteNtseStudent;
import com.nsp.backend.model.NtseForm;

@RestController
@RequestMapping("/api/studentslist")
@CrossOrigin(origins="http://localhost:3000")
public class InstituteNtseController {

    @Autowired
    private CombinedFormRepository combinedFormRepository;
    @Autowired
    private NtseFormRepository ntseFormRepository;

    // Get all students whose email is present in both tables and marks > 90
    @GetMapping("/getall")
    public List<InstituteNtseStudent> getAllStudents() {
        List<CombinedForm> combinedForms = combinedFormRepository.findAll();
        List<NtseForm> ntseForms = ntseFormRepository.findAll();

        // Create a map of emails to marks from the NtseForm repository
        Map<String, Integer> ntseEmailToMarks = ntseForms.stream()
                .filter(ntseForm -> ntseForm.getMarks() > 90) // Filter by marks > 90
                .collect(Collectors.toMap(NtseForm::getEmail, NtseForm::getMarks));

        // Filter combinedForms to include only those emails present in both repositories and marks > 90
        List<InstituteNtseStudent> result = combinedForms.stream()
                .filter(cf -> ntseEmailToMarks.containsKey(cf.getEmail()))
                .map(cf -> new InstituteNtseStudent(
                        cf.getFirstName() + " " + cf.getMiddleName() + " " + cf.getLastName(),
                        cf.getEmail()
                ))
                .collect(Collectors.toList());

        // Log the size of the result list
        System.out.println("Result size: " + result.size());

        return result;
    }

    // Delete a student
    @DeleteMapping("delete/{email}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String email) {
        if (ntseFormRepository.existsById(email)) {
        	ntseFormRepository.deleteById(email);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
