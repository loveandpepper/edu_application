package org.hofftech.edu.service.packingstategy;

import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;

import java.util.List;

/**
 * Интерфейс для реализации стратегий упаковки.
 * Определяет метод для размещения посылок в грузовиках.
 */
public interface DefaultPackingStrategy {
    /**
     * Размещает посылки в грузовиках согласно определённой стратегии.
     *
     * @param packages         список посылок для упаковки
     * @param useEasyAlgorithm флаг использования упрощённого алгоритма
     * @param useEvenAlgorithm флаг использования алгоритма равномерного распределения
     * @param trucksFromArgs   список размеров грузовиков, заданных пользователем
     * @return список грузовиков с размещёнными посылками
     */
    List<Truck> addPackages(List<Package> packages, boolean useEasyAlgorithm, boolean useEvenAlgorithm, List<String> trucksFromArgs);
}
