package org.hofftech.edu.service.packingstategy;

import lombok.AllArgsConstructor;
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
     * @param useEasyAlgorithm флаг использования упрощённого алгоритма
     * @return объект стратегии упаковки
     */
    public DefaultPackingStrategy getStrategy(boolean useEasyAlgorithm) {
        if (useEasyAlgorithm) {
            return new IndividualTruckStrategy(truckService);
        } else {
            return new MultipleTruckStrategy(truckService);
        }
    }
}
