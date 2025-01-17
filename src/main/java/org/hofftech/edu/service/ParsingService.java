package org.hofftech.edu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.PackageArgumentException;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.repository.PackageRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для разбора данных о посылках.
 * Обрабатывает строки из файла или аргументы командной строки и преобразует их в объекты Package.
 */
@Slf4j
@RequiredArgsConstructor
public class ParsingService {
    private static final String COMMA_REPLACEMENTS = "[“”\"]";
    private static final String PARCELS_SPLITTER = ",";
    private final PackageRepository packageRepository;

    /**
     * Разбирает строки из файла и преобразует их в список объектов Package.
     *
     * @param lines список строк из файла
     * @return список объектов Package
     */
    public List<Package> parsePackagesFromFile(List<String> lines) {
        List<Package> packages = new ArrayList<>();
        for (String packageName : lines) {
            String trimmedName = packageName.trim().replaceAll(COMMA_REPLACEMENTS, "");
            if (trimmedName.isEmpty()) {
                continue;
            }
            packageRepository.findPackage(trimmedName).ifPresentOrElse(
                    packages::add,
                    () -> {
                        throw new RuntimeException("Посылка " + trimmedName + " не найдена!");
                    }
            );
        }
        return packages;
    }


    /**
     * Разбирает текст с именами посылок, переданный как аргумент, и преобразует его в список объектов Package.
     *
     * @param parcelsText текстовый ввод с именами посылок, разделёнными запятыми
     * @return список объектов Package
     * @throws IllegalArgumentException если текст пустой или null
     */
    public List<Package> getPackagesFromArgs(String parcelsText) {
        if (parcelsText == null || parcelsText.isBlank()) {
            throw new PackageArgumentException("Аргумент с посылками пуст");
        }
        List<Package> packages = new ArrayList<>();
        String[] names = parcelsText.split(PARCELS_SPLITTER);
        for (String name : names) {
            String trimmedName = name.trim();
            if (!trimmedName.isEmpty()) {
                packageRepository.findPackage(trimmedName).ifPresentOrElse(
                        packages::add,
                        () -> {
                            throw new PackageNotFoundException(
                                    "Посылка '" + trimmedName + "' из аргументов не найдена и не может быть обработана"
                            );
                        }
                );
            }
        }
        return packages;
    }
}
