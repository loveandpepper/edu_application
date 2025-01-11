package org.hofftech.edu.service.packingstategy;

import lombok.AllArgsConstructor;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.service.TruckService;

import java.util.List;

/**
 * Реализация стратегии упаковки, предполагающей использование нескольких грузовиков.
 * Посылки размещаются в нескольких грузовиках, учитывая равномерное распределение или плотное размещение.
 */
@AllArgsConstructor
public class MultipleTruckStrategy implements DefaultPackingStrategy {
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
