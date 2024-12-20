package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.model.Package;

import java.util.List;

@Slf4j
public class PackingService {
    public boolean canAddPackage(Truck truck, Package pkg, int startX, int startY) {
        log.debug("Проверяем возможность добавить упаковку {} в координаты X={}, Y={}", pkg.getType(), startX, startY);
        List<String> shape = pkg.getType().getShape();
        int height = shape.size();

        for (int y = 0; y < height; y++) {
            int rowWidth = shape.get(y).length();
            if (startX + rowWidth > truck.getWIDTH() || startY + y >= truck.getHEIGHT()) {
                log.debug("Упаковка {} выходит за пределы грузовика", pkg.getType());
                return false;
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < shape.get(y).length(); x++) {
                if (shape.get(y).charAt(x) != ' ' && truck.getGrid()[startY + y][startX + x] != ' ') {
                    log.debug("Упаковка {} пересекается с другой упаковкой", pkg.getType());
                    return false;
                }
            }
        }

        return true;
    }

    private boolean hasSufficientSupport(Truck truck, Package pkg, int startX, int startY) {
        List<String> shape = pkg.getType().getShape();
        int width = shape.getLast().length(); // Нижняя строка упаковки
        int supportCount = 0;

        if (startY == truck.getHEIGHT() - shape.size()) {
            // Упаковка на дне, дополнительная проверка не нужна
            return true;
        }

        // Проверяем опору снизу
        for (int x = 0; x < width; x++) {
            if (shape.getLast().charAt(x) != ' ' &&
                    truck.getGrid()[startY + shape.size()][startX + x] != ' ') {
                supportCount++;
            }
        }

        return supportCount >= (width + 1) / 2; // Опора не менее половины ширины
    }

    public boolean addPackage(Truck truck, Package pkg) {
        log.info("Пытаемся добавить упаковку {} в грузовик", pkg.getType());
        List<String> shape = pkg.getType().getShape();
        int height = shape.size();

        for (int startY = truck.getHEIGHT() - height; startY >= 0; startY--) {
            for (int startX = 0; startX <= truck.getWIDTH() - 1; startX++) {
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
        List<String> shape = pkg.getType().getShape();

        for (int y = 0; y < shape.size(); y++) {
            for (int x = 0; x < shape.get(y).length(); x++) {
                if (shape.get(y).charAt(x) != ' ') {
                    truck.getGrid()[startY + y][startX + x] = shape.get(y).charAt(x);
                }
            }
        }
        truck.getPackages().add(pkg);
        log.info("Упаковка {} размещена на грузовике", pkg.getType());
    }

}


