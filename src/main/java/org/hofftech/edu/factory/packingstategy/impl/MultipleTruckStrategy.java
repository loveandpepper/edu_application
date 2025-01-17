package org.hofftech.edu.factory.packingstategy.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.factory.packingstategy.PackingStrategy;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.service.TruckService;

import java.util.List;

/**
 * Реализация стратегии упаковки, предполагающей использование нескольких грузовиков.
 * Посылки размещаются в нескольких грузовиках, учитывая равномерное распределение или плотное размещение.
 */
@RequiredArgsConstructor
public class MultipleTruckStrategy implements PackingStrategy {
    private final TruckService truckService;

    /**
     * Размещает посылки в нескольких грузовиках.
     *
     * @param packages         список посылок для упаковки
     * @param useEasyAlgorithm флаг использования упрощённого алгоритма
     * @param useEvenAlgorithm флаг использования алгоритма равномерного распределения
     * @param trucksFromArgs   список размеров грузовиков, заданных пользователем
     * @return список грузовиков с размещёнными посылками
     */
    @Override
    public List<Truck> addPackages(List<Package> packages, boolean useEasyAlgorithm, boolean useEvenAlgorithm, List<String> trucksFromArgs) {
        return truckService.addPackagesToMultipleTrucks(packages, useEvenAlgorithm, trucksFromArgs);
    }
}
