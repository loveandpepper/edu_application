package org.hofftech.edu.service.filesaving;

import org.hofftech.edu.model.Truck;
import java.util.List;

public interface FileSavingAlgorithm {
    void save(List<Truck> trucks, String outputPath) throws Exception;
}
