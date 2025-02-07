package org.hofftech.edu.repository;

import org.hofftech.edu.model.Package;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface PackageRepository extends JpaRepository<Package, String> {

    /**
     * Доп. метод, чтобы вернуть именно List<Package>
     * при постраничном запросе: findAll(Pageable) по умолчанию
     * даёт Page<Package>.
     */
    default List<Package> getAllPackages(Pageable pageable) {
        return findAll(pageable).getContent();
    }
}
