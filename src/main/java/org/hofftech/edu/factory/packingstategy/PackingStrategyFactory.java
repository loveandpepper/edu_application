package org.hofftech.edu.factory.packingstategy;

import lombok.AllArgsConstructor;
import org.hofftech.edu.factory.packingstategy.impl.IndividualTruckStrategy;
import org.hofftech.edu.factory.packingstategy.impl.MultipleTruckStrategy;
import org.hofftech.edu.service.TruckService;

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
