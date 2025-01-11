package org.hofftech.edu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            String trimmedName = packageName.trim().replaceAll("[“”\"]", "");
            if (trimmedName.isEmpty()) {
                continue;
            }
            try {
                Package pkg = packageRepository.findPackage(trimmedName);
                packages.add(pkg);
            } catch (IllegalArgumentException e) {
                log.warn("Посылка '{}' из файла  не найдена и будет пропущена.", trimmedName);
            }
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
            log.warn("Текст посылок пустой.");
            throw new IllegalArgumentException("Аргумент с посылками пуст");
        }

        List<Package> packages = new ArrayList<>();
        String[] names = parcelsText.split(",");

        for (String name : names) {
            String trimmedName = name.trim();
            if (!trimmedName.isEmpty()) {
                try {
                    Package pkg = packageRepository.findPackage(trimmedName);
                    packages.add(pkg);
                } catch (IllegalArgumentException e) {
                    log.warn("Посылка '{}' из аргументов не найдена и будет пропущена.", trimmedName);
                }
            }
        }

        return packages;
    }
}
