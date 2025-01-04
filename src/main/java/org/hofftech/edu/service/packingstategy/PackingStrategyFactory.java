package org.hofftech.edu.service.packingstategy;

import lombok.AllArgsConstructor;
import org.hofftech.edu.service.TruckService;

@AllArgsConstructor
public class PackingStrategyFactory {
    private final TruckService truckService;

    public PackingStrategy getStrategy(boolean useEasyAlgorithm) {
        if (useEasyAlgorithm) {
            return new IndividualTruckStrategy(truckService);
        } else {
            return new MultipleTruckStrategy(truckService);
        }
    }
}
