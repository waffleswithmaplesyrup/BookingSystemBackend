package com.example.BookingSystemBackend.Service;

import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Model.ClassInfo;
import com.example.BookingSystemBackend.Model.PackageBundle;
import com.example.BookingSystemBackend.Repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    @Autowired
    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public List<ClassInfo> viewAllClasses(Country country) {
        return classRepository.findAllByCountry(country);
    }
}
