package org.hofftech.edu.console.service;

import org.hofftech.edu.console.model.dto.BillingCommandDto;
import org.hofftech.edu.console.model.dto.CreateCommandDto;
import org.hofftech.edu.console.model.dto.LoadCommandDto;
import org.hofftech.edu.console.model.dto.UnloadCommandDto;
import org.hofftech.edu.console.model.dto.UpdateCommandDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CommandParserTest {

    @InjectMocks
    private CommandParser commandParser;

    @Test
    void parseToCreateDto_shouldParseNameFormAndSymbol() {
        String command = "+name \"Test Name\" +form \"Test Form\" +symbol #";

        CreateCommandDto dto = commandParser.parseToCreateDto(command);

        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo("Test Name");
        assertThat(dto.getForm()).isEqualTo("Test Form");
        assertThat(dto.getSymbol()).isEqualTo("#");
    }

    @Test
    void parseToUpdateDto_shouldParseOldNameNameFormAndSymbol() {
        String command = "+oldName \"Old Name\" +name \"New Name\" +form \"New Form\" +symbol @";

        UpdateCommandDto dto = commandParser.parseToUpdateDto(command);

        assertThat(dto).isNotNull();
        assertThat(dto.getOldName()).isEqualTo("Old Name");
        assertThat(dto.getName()).isEqualTo("New Name");
        assertThat(dto.getForm()).isEqualTo("New Form");
        assertThat(dto.getSymbol()).isEqualTo("@");
    }

    @Test
    void parseToLoadDto_shouldParseAllLoadFields() {
        String command =
                "+parcelsText \"Some parcels\" " +
                        "+trucks \"Truck1,Truck2\" " +
                        "+user JohnDoe " +
                        "+even true +easy false +save true";

        LoadCommandDto dto = commandParser.parseToLoadDto(command);

        assertThat(dto).isNotNull();
        assertThat(dto.getParcelsText()).isEqualTo("Some parcels");
        assertThat(dto.getTrucks()).isEqualTo("Truck1,Truck2");
        assertThat(dto.getUser()).isEqualTo("JohnDoe");
        assertThat(dto.isEven()).isTrue();
        assertThat(dto.isEasy()).isFalse();
        assertThat(dto.isSave()).isTrue();
    }

    @Test
    void parseToUnloadDto_shouldParseInFileUserAndWithCount() {
        String command = "+inFile output.txt +user JaneDoe +withCount true";

        UnloadCommandDto dto = commandParser.parseToUnloadDto(command);

        assertThat(dto).isNotNull();
        assertThat(dto.getInFile()).isEqualTo("output.txt");
        assertThat(dto.getUser()).isEqualTo("JaneDoe");
        assertThat(dto.isWithCount()).isTrue();
    }

    @Test
    void parseToBillingDto_shouldParseUserAndDateRange() {
        String command = "+user BillUser +from 2023-01-01 +to 2023-12-31";

        BillingCommandDto dto = commandParser.parseToBillingDto(command);

        assertThat(dto).isNotNull();
        assertThat(dto.getUser()).isEqualTo("BillUser");
        assertThat(dto.getFrom()).isEqualTo("2023-01-01");
        assertThat(dto.getTo()).isEqualTo("2023-12-31");
    }
}
