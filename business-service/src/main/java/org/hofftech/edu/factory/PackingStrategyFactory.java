package org.hofftech.edu.factory;

import lombok.AllArgsConstructor;
import org.hofftech.edu.service.TruckService;
import org.hofftech.edu.service.packingstategy.PackingStrategy;
import org.hofftech.edu.service.packingstategy.impl.IndividualTruckStrategy;
import org.hofftech.edu.service.packingstategy.impl.MultipleTruckStrategy;

/**
 * Фабрика для создания стратегий упаковки.
 * Определяет, какая стратегия должна быть выбрана в зависимости от параметров.
 */
@AllArgsConstructor
public class PackingStrategyFactory {
    private final TruckService truckService;

    /**
     * Возвращает стратегию упаковки на основе выбранного алгоритма.
     *
     * @param ifEasyAlgorithm флаг использования упрощённого алгоритма
     * @return объект стратегии упаковки
     */
    public PackingStrategy createStrategy(boolean ifEasyAlgorithm) {
        if (ifEasyAlgorithm) {
            return new IndividualTruckStrategy(truckService);
        } else {
            return new MultipleTruckStrategy(truckService);
        }
    }
}
