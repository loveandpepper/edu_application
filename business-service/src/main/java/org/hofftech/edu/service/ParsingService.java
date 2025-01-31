package org.hofftech.edu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.PackageArgumentException;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.exception.TxtLoadException;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.repository.PackageRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для разбора данных о посылках.
 */
@Slf4j
@RequiredArgsConstructor
public class ParsingService {

    private static final String COMMA_REPLACEMENTS = "[“”\"]";
    private static final String PARCELS_SPLITTER = ",";
    private final PackageRepository packageRepository;

    public List<Package> parsePackagesFromFile(List<String> lines) {
        List<Package> packages = new ArrayList<>();
        for (String packageName : lines) {
            String trimmedName = packageName.trim().replaceAll(COMMA_REPLACEMENTS, "");
            if (trimmedName.isEmpty()) {
                continue;
            }
            packageRepository.findById(trimmedName)
                    .ifPresentOrElse(
                            packages::add,
                            () -> {
                                throw new TxtLoadException("Посылка " + trimmedName + " не найдена!");
                            }
                    );
        }
        return packages;
    }

    public List<Package> getPackagesFromArgs(String parcelsText) {
        if (parcelsText == null || parcelsText.isBlank()) {
            throw new PackageArgumentException("Аргумент с посылками пуст");
        }
        List<Package> packages = new ArrayList<>();
        String[] names = parcelsText.split(PARCELS_SPLITTER);
        for (String name : names) {
            String trimmedName = name.trim();
            if (!trimmedName.isEmpty()) {
                packageRepository.findById(trimmedName)
                        .ifPresentOrElse(
                                packages::add,
                                () -> {
                                    throw new PackageNotFoundException(
                                            "Посылка '" + trimmedName + "' из аргументов не найдена"
                                    );
                                }
                        );
            }
        }
        return packages;
    }
}
