package org.hofftech.service;

import org.hofftech.model.Package;
import org.hofftech.model.PackageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ValidatorService {
    private static final Logger log = LoggerFactory.getLogger(ValidatorService.class);

    public boolean validateFile(List<String> lines) {
        if (lines.isEmpty()) {
            log.error("Файл пустой или не содержит данных.");
            return false;
        }
        log.info("Файл успешно проверен. Количество строк: {}", lines.size());
        return true;
    }

    public boolean validatePackages(List<Package> packages) {
        for (Package pkg : packages) {
            if (!isValidPackage(pkg)) {
                log.error("Упаковка с ID {} имеет некорректную форму: {}", pkg.getId(), pkg.getType().getShape());
                return false;
            }
        }
        log.info("Все упаковки успешно проверены.");
        return true;
    }

    private boolean isValidPackage(Package pkg) {
        PackageType type = pkg.getType();
        String[] shape = type.getShape();

        // Проверяем, что форма упаковки не имеет "выпадающих" символов
        int maxWidth = 0;
        for (String row : shape) {
            maxWidth = Math.max(maxWidth, row.length());
        }

        for (String row : shape) {
            // Если в строке справа от длины строки есть символы, это ошибка
            if (row.length() < maxWidth) {
                for (int x = row.length(); x < maxWidth; x++) {
                    if (row.length() > x) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
