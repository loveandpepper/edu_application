package org.hofftech.edu.service.packingstategy;

import lombok.AllArgsConstructor;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.service.TruckService;

import java.util.List;

@AllArgsConstructor
public class IndividualTruckStrategy implements PackingStrategy{
    private final TruckService truckService;

    @Override
    public List<Truck> addPackages(List<Package> packages, int maxTrucks, boolean useEasyAlgorithm, boolean useEvenAlgorithm) {
        return truckService.addPackagesToIndividualTrucks(packages);
    }
}
