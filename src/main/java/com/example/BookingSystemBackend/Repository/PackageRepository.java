package com.example.BookingSystemBackend.Repository;

import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Model.PackageBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<PackageBundle, Long> {
    public List<PackageBundle> findAllByCountry(Country country);
}
