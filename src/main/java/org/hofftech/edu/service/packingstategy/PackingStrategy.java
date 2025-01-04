package org.hofftech.edu.service.packingstategy;

import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;

import java.util.List;

public interface PackingStrategy {
    List<Truck> addPackages(List<Package> packages, int maxTrucks, boolean useEasyAlgorithm, boolean useEvenAlgorithm);
}
