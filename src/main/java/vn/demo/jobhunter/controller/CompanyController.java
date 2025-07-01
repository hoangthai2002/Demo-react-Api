package vn.demo.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.demo.jobhunter.domain.Company;
import vn.demo.jobhunter.domain.response.ResultPaginationDTO;
import vn.demo.jobhunter.service.CompanyService;
import vn.demo.jobhunter.util.annotation.ApiMessage;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("Create company Success")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        this.companyService.createdCompanyService(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    // fetch all company
    @GetMapping("/companies")
    @ApiMessage("Get Company Success")
    public ResponseEntity<ResultPaginationDTO> getCompanyAll(
            @Filter Specification<Company> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.getCompanyAllService(spec, pageable));
    }

    @PutMapping("/companies")
    @ApiMessage("put company success")
    public ResponseEntity<Company> putCompany(@RequestBody Company reCompany) {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.putCompanyService(reCompany));
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("Delete company Success")
    public ResponseEntity<Void> delateCompany(@PathVariable("id") long id) {
        this.companyService.deleteCompanyService(id);
        return ResponseEntity.ok().body(null);

    }

}
