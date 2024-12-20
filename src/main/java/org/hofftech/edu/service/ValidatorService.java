package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageType;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ValidatorService {
    public boolean isValidFile(List<String> lines) {
        if (CollectionUtils.isEmpty(lines)) {
            log.error("Файл пустой или не содержит данных.");
            return false;
        }
        log.info("Файл успешно проверен. Количество строк: {}", lines.size());
        return true;
    }

    public boolean isValidPackages(List<Package> packages) {
        List<Package> invalidPackages = new ArrayList<>();
        for (Package pkg : packages) {
            if (!isValidPackage(pkg)) {
                invalidPackages.add(pkg);
            }
        }
        if (!invalidPackages.isEmpty()) {
            for (Package invalidPkg : invalidPackages) {
                log.error("Упаковка с ID {} имеет некорректную форму: {}",
                        invalidPkg.getId(), invalidPkg.getType().getShape());
            }
            return false;
        }

        log.info("Все упаковки успешно проверены.");
        return true;
    }

    private boolean isValidPackage(Package pkg) {
        PackageType type = pkg.getType();
        List<String> shape = type.getShape();

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
