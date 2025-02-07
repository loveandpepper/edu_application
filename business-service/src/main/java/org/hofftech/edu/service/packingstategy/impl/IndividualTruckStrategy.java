package org.hofftech.edu.service.packingstategy.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.service.TruckService;
import org.hofftech.edu.service.packingstategy.PackingStrategy;

import java.util.List;

/**
 * Реализация стратегии упаковки, предполагающей использование индивидуальных грузовиков для каждой посылки.
 */
@RequiredArgsConstructor
public class IndividualTruckStrategy implements PackingStrategy {

    private final TruckService truckService;
    /**
     * Размещает каждую посылку в индивидуальном грузовике.
     *
     * @param packages         список посылок для упаковки
     * @param isEasyAlgorithm флаг использования упрощённого алгоритма
     * @param isEvenAlgorithm флаг использования алгоритма равномерного распределения
     * @param trucksFromArgs   список размеров грузовиков, заданных пользователем
     * @return список грузовиков с размещёнными посылками
     */
    @Override
    public List<Truck> addPackages(List<Package> packages, boolean isEasyAlgorithm, boolean isEvenAlgorithm, List<String> trucksFromArgs) {
        return truckService.addPackagesToIndividualTrucks(packages, trucksFromArgs);
    }
}
