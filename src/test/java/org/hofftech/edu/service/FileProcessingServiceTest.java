package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.hofftech.edu.factory.packingstategy.PackingStrategy;
import org.hofftech.edu.factory.packingstategy.PackingStrategyFactory;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class FileProcessingServiceTest {

    private FileProcessingService fileProcessingService;
    private ParsingService parsingServiceMock;
    private ValidatorService validatorServiceMock;
    private TruckService truckServiceMock;
    private JsonProcessingService jsonProcessingServiceMock;
    private PackingStrategyFactory packingStrategyFactoryMock;

    @BeforeEach
    void setUp() {
        parsingServiceMock = Mockito.mock(ParsingService.class);
        validatorServiceMock = Mockito.mock(ValidatorService.class);
        truckServiceMock = Mockito.mock(TruckService.class);
        jsonProcessingServiceMock = Mockito.mock(JsonProcessingService.class);
        packingStrategyFactoryMock = Mockito.mock(PackingStrategyFactory.class);

        fileProcessingService = new FileProcessingService(
                parsingServiceMock,
                validatorServiceMock,
                truckServiceMock,
                jsonProcessingServiceMock,
                packingStrategyFactoryMock
        );
    }

    @Test
    void shouldProcessArgumentsSuccessfully() {
        String parcelsText = "Посылка Тип 4, Посылка Тип 3";
        List<String> trucksFromArgs = List.of("6x6");
        PackingStrategy packingStrategyMock = Mockito.mock(PackingStrategy.class);

        Mockito.when(parsingServiceMock.getPackagesFromArgs(parcelsText))
                .thenReturn(List.of(new Package("Посылка Тип 4", null, '4', null),
                        new Package("Посылка Тип 3", null, '3', null)));
        Mockito.when(packingStrategyFactoryMock.createStrategy(false))
                .thenReturn(packingStrategyMock);
        Mockito.when(packingStrategyMock.addPackages(Mockito.anyList(), Mockito.anyBoolean(), Mockito.anyBoolean(),
                        Mockito.anyList()))
                .thenReturn(List.of(new Truck(6, 6)));
        Mockito.when(truckServiceMock.printTrucks(Mockito.anyList()))
                .thenReturn("Truck output");

        String result = fileProcessingService.processFile(null, parcelsText, trucksFromArgs, false,
                false, false);

        Assertions.assertThat(result).isEqualTo("Truck output");
    }

    @Test
    void shouldThrowExceptionForEmptyPackages() {
        String parcelsText = "";

        Mockito.when(parsingServiceMock.getPackagesFromArgs(parcelsText))
                .thenReturn(List.of());

        Assertions.assertThatThrownBy(() -> fileProcessingService.processFile(null, parcelsText, List.of(),
                        false, false, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Упаковки не представлены, продолжение работы невозможно");
    }
}

