package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;

/**
 * Сервис для упаковки посылок в грузовики.
 * Отвечает за проверку возможности добавления упаковки и размещение её в грузовике.
 */
@Slf4j
public class PackingService {

    private static final int START_Y_POSITION = 0;
    private static final int START_X_POSITION = 0;
    private static final char EMPTY_SPACE = ' ';
    private static final int ROW_LENGTH_FIRST_SYMBOL = 0;

    /**
     * Проверяет, может ли упаковка быть добавлена в указанную позицию грузовика.
     *
     * @param truck грузовик, куда нужно добавить упаковку
     * @param providedPackage   упаковка для добавления
     * @param startX координата X начальной позиции
     * @param startY координата Y начальной позиции
     * @return true, если упаковка может быть добавлена, иначе false
     */
    private boolean canAddPackage(Truck truck, Package providedPackage, int startX, int startY) {
        log.debug("Проверяем возможность добавить упаковку {} в координаты X={}, Y={}", providedPackage.getName(), startX, startY);
        String[] shape = providedPackage.getReversedShape();
        int height = shape.length;

        if (isPackageWithinLimits(truck, providedPackage, startX, startY, height, shape)) return false;

        if (isIntersection(truck, providedPackage, startX, startY, height, shape)) return false;

        String topRow = shape[0];
        double requiredSupport = Math.ceil(topRow.length() / 2.0);
        int support = 0;
        if (startY == 0) {
            log.debug("Упаковка {} внизу грузовика, опора не требуется.", providedPackage.getName());
            return true;
        }

        return !isPackageSupported(truck, providedPackage, startX, startY, topRow, support, requiredSupport);
    }

    private boolean isPackageSupported(Truck truck, Package pkg, int startX, int startY, String topRow, int support, double requiredSupport) {
        for (int x = ROW_LENGTH_FIRST_SYMBOL; x < topRow.length(); x++) {
            if (topRow.charAt(x) != EMPTY_SPACE && truck.getGrid()[startY - 1][startX + x] != EMPTY_SPACE) {
                support++;
            }
        }
        if (support < requiredSupport) {
            log.debug("Упаковка {} не имеет достаточной опоры. Требуется {}, доступно {}", pkg.getName(), requiredSupport, support);
            return true;
        }
        return false;
    }

    private boolean isIntersection(Truck truck, Package pkg, int startX, int startY, int height, String[] shape) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < shape[y].length(); x++) {
                if (shape[y].charAt(x) != ' ' && truck.getGrid()[startY + y][startX + x] != ' ') {
                    log.debug("Упаковка {} пересекается с другой посылкой", pkg.getName());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPackageWithinLimits(Truck truck, Package pkg, int startX, int startY, int height, String[] shape) {
        for (int y = 0; y < height; y++) {
            int rowWidth = shape[y].length();
            if (startX + rowWidth > truck.getWidth() || startY + y >= truck.getHeight()) {
                log.debug("Упаковка {} выходит за пределы грузовика", pkg.getName());
                return true;
            }
        }
        return false;
    }


    /**
     * Пытается добавить упаковку в грузовик.
     *
     * @param truck грузовик, куда нужно добавить упаковку
     * @param providedPackage   упаковка для добавления
     * @return true, если упаковка успешно добавлена, иначе false
     */
    public boolean tryPack(Truck truck, Package providedPackage) {
        log.info("Пытаемся добавить упаковку {} в грузовик.", providedPackage.getName());

        String[] shape = providedPackage.getReversedShape();
        int height = shape.length;

        for (int startY = START_Y_POSITION; startY <= truck.getHeight() - height; startY++) {
            for (int startX = START_X_POSITION; startX <= truck.getWidth() - shape[0].length(); startX++) {
                if (canAddPackage(truck, providedPackage, startX, startY)) {
                    log.info("Упаковка {} успешно добавлена", providedPackage.getName());
                    providedPackage.setStartPositionX(startX);
                    providedPackage.setStartPositionY(startY);
                    placePackage(truck, providedPackage, startX, startY);
                    return true;
                }
            }
        }

        log.warn("Упаковка {} не смогла быть добавлена в грузовик.", providedPackage.getName());
        return false;
    }

    /**
     * Размещает упаковку в указанной позиции грузовика.
     *
     * @param truck грузовик, в котором размещается упаковка
     * @param providedPackage   упаковка для размещения
     * @param startX координата X начальной позиции
     * @param startY координата Y начальной позиции
     */
    private void placePackage(Truck truck, Package providedPackage, int startX, int startY) {
        String[] shape = providedPackage.getReversedShape();

        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length(); x++) {
                if (shape[y].charAt(x) != ' ') {
                    truck.getGrid()[startY + y][startX + x] = shape[y].charAt(x);
                }
            }
        }
        providedPackage.setStartPositionX(startX);
        providedPackage.setStartPositionY(startY);
        truck.getPackages().add(providedPackage);
        log.info("Упаковка {} размещена на грузовике", providedPackage.getName());
    }


}
