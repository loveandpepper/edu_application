package org.hofftech.service;

import org.hofftech.model.Truck;
import org.hofftech.model.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackingService {
    private static final Logger log = LoggerFactory.getLogger(PackingService.class);


    public boolean canAddPackage(Truck truck, Package pkg, int startX, int startY) {
        log.debug("Проверяем возможность добавить упаковку {} в координаты X={}, Y={}", pkg.getType(), startX, startY);
        String[] shape = pkg.getType().getShape();
        int height = shape.length;

        for (int y = 0; y < height; y++) {
            int rowWidth = shape[y].length();
            if (startX + rowWidth > Truck.getWIDTH() || startY + y >= Truck.getHEIGHT()) {
                log.debug("Упаковка {} выходит за пределы грузовика", pkg.getType());
                return false;
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < shape[y].length(); x++) {
                if (shape[y].charAt(x) != ' ' && truck.getGrid()[startY + y][startX + x] != ' ') {
                    log.debug("Упаковка {} пересекается с другой упаковкой", pkg.getType());
                    return false;
                }
            }
        }

        return true;
    }

    private boolean hasSufficientSupport(Truck truck, Package pkg, int startX, int startY) {
        String[] shape = pkg.getType().getShape();
        int width = shape[shape.length - 1].length(); // Нижняя строка упаковки
        int supportCount = 0;

        if (startY == Truck.getHEIGHT() - shape.length) {
            // Упаковка на дне, дополнительная проверка не нужна
            return true;
        }

        // Проверяем опору снизу
        for (int x = 0; x < width; x++) {
            if (shape[shape.length - 1].charAt(x) != ' ' &&
                    truck.getGrid()[startY + shape.length][startX + x] != ' ') {
                supportCount++;
            }
        }

        return supportCount >= (width + 1) / 2; // Опора не менее половины ширины
    }

    public boolean addPackage(Truck truck, Package pkg) {
        log.info("Пытаемся добавить упаковку {} в грузовик", pkg.getType());
        String[] shape = pkg.getType().getShape();
        int height = shape.length;

        for (int startY = Truck.getHEIGHT() - height; startY >= 0; startY--) {
            for (int startX = 0; startX <= Truck.getWIDTH() - 1; startX++) {
                if (canAddPackage(truck, pkg, startX, startY)) {
                    if (hasSufficientSupport(truck, pkg, startX, startY)) {
                        log.info("Упаковка {} успешно добавлена в координаты X={}, Y={}", pkg.getType(), startX, startY);
                        placePackage(truck, pkg, startX, startY);
                        return true;
                    }
                }
            }
        }

        log.warn("Не удалось добавить упаковку {}", pkg.getType());
        return false;
    }

    private void placePackage(Truck truck, Package pkg, int startX, int startY) {
        String[] shape = pkg.getType().getShape();

        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length(); x++) {
                if (shape[y].charAt(x) != ' ') {
                    truck.getGrid()[startY + y][startX + x] = shape[y].charAt(x);
                }
            }
        }
        truck.getPackages().add(pkg);
        log.info("Упаковка {} размещена на грузовике", pkg.getType());
    }

}


