package vn.demo.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.demo.jobhunter.domain.Company;
import vn.demo.jobhunter.domain.dto.Meta;
import vn.demo.jobhunter.domain.dto.ResultPaginationDTO;
import vn.demo.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createdCompanyService(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO getCompanyAllService(Specification<Company> spec, Pageable pageable) {

        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageCompany.getContent());

        return rs;
    }

    public void deleteCompanyService(Long id) {
        this.companyRepository.deleteById(id);
    }

    public Company putCompanyService(Company company) {
        Optional<Company> companyOptional = this.companyRepository.findById(company.getId());
        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setLogo(company.getLogo());
            currentCompany.setName(company.getName());
            currentCompany.setDescription(company.getDescription());
            currentCompany.setAddress(company.getAddress());

            return this.companyRepository.save(currentCompany);

        }
        return null;
    }

}
