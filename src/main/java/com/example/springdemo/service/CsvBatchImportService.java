package com.example.springdemo.service;

import com.example.springdemo.entity.Department;
import com.example.springdemo.entity.Employee;
import com.example.springdemo.repository.DepartmentRepository;
import com.example.springdemo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.springdemo.utility.Util.*;
import static com.example.springdemo.utility.Util.parseIntSafe;


@Service
public class CsvBatchImportService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public CsvBatchImportService(EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

        @Transactional
        public void importCsvBatch(MultipartFile file) throws Exception {

        List<Employee> batch = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // salta righe vuote
                if (line.trim().isEmpty()) {
                    continue;
                }

                // salta intestazione
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // split robusto su virgola con spazi
                String[] fields = line.split("\\s*,\\s*");

                // riga incompleta → ignora
                if (fields.length < 10) {
                    System.out.println("Riga ignorata perché incompleta: " + line);
                    continue;
                }

                // pulizia base
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                // Parsing sicuro
                Long empno = fields[0] != null && fields[0].trim().matches("-?\\d+")
                        ? Long.valueOf(fields[0].trim())
                        : null;
                String ename = fields[1];
                String job = fields[2];
                Integer mgr = parseIntSafe(fields[3]);
                LocalDate hiredate = parseDateSafe(fields[4]);
                Double sal = parseDoubleSafe(fields[5]);
                Double comm = parseDoubleSafe(fields[6]);
                Integer deptno = parseIntSafe(fields[7]);
                String deptname = fields[8];
                String deptdesc = fields[9];

                // Recupera o crea il dipartimento
                Department dept = departmentRepository.findById(deptno)
                        .orElseGet(() -> {
                            Department d = new Department();
                            d.setDeptno(deptno);
                            d.setDname(deptname);
                            d.setLocation(deptdesc);
                            return departmentRepository.save(d);
                        });

                // Crea Employee
                Employee emp = new Employee();
                emp.setEmpno(empno);
                emp.setEname(ename);
                emp.setJob(job);
                emp.setMgr(mgr);
                emp.setHiredate(hiredate);
                emp.setSal(sal);
                emp.setComm(comm);
                emp.setDepartment(dept);

                batch.add(emp);

                // Salvataggio batch
                if (batch.size() == BATCH_SIZE) {
                    employeeRepository.saveAll(batch);
                    batch.clear();
                }
            }

            // Salva gli ultimi record
            if (!batch.isEmpty()) {
                employeeRepository.saveAll(batch);
            }
        }
    }
}