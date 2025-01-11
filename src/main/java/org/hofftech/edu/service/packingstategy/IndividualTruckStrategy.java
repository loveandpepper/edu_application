package org.hofftech.edu.service.packingstategy;

import lombok.AllArgsConstructor;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.service.TruckService;

import java.util.List;

/**
 * Реализация стратегии упаковки, предполагающей использование индивидуальных грузовиков для каждой посылки.
 */
@AllArgsConstructor
public class IndividualTruckStrategy implements DefaultPackingStrategy {
    private final TruckService truckService;

    /**
     * Размещает каждую посылку в индивидуальном грузовике.
     *
     * @param packages         список посылок для упаковки
     * @param useEasyAlgorithm флаг использования упрощённого алгоритма
     * @param useEvenAlgorithm флаг использования алгоритма равномерного распределения
     * @param trucksFromArgs   список размеров грузовиков, заданных пользователем
     * @return список грузовиков с размещёнными посылками
     */
    @Override
    public List<Truck> addPackages(List<Package> packages, boolean useEasyAlgorithm, boolean useEvenAlgorithm, List<String> trucksFromArgs) {
        return truckService.addPackagesToIndividualTrucks(packages, trucksFromArgs);
    }
}
