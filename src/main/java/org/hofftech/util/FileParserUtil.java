package org.hofftech.util;

import org.hofftech.model.PackageType;
import org.hofftech.model.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FileParserUtil {
    private static final Logger log = LoggerFactory.getLogger(FileParserUtil.class);

    public List<Package> parsePackages(List<String> lines) {
        List<Package> packages = new ArrayList<>();
        List<String> currentShape = new ArrayList<>();
        int packageId = 1;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (!currentShape.isEmpty()) {
                    Package pkg = createPackage(currentShape, packageId++);
                    if (pkg != null) {
                        packages.add(pkg);
                    }
                    currentShape.clear();
                }
            } else {
                currentShape.add(line);
            }
        }

        // Обработка последней упаковки
        if (!currentShape.isEmpty()) {
            Package pkg = createPackage(currentShape, packageId++);
            if (pkg != null) {
                packages.add(pkg);
            }
        }

        log.info("Успешно распознано {} упаковок.", packages.size());
        return packages;
    }

    private Package createPackage(List<String> shapeLines, int id) {
        try {
            PackageType packageType = PackageType.fromShape(shapeLines.toArray(new String[0]));
            return new Package(packageType, id);
        } catch (Exception e) {
            log.error("Ошибка при создании упаковки с ID {}: {}", id, e.getMessage());
            return null;
        }
    }
}
