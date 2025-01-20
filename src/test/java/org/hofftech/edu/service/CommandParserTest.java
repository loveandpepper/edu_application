package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.hofftech.edu.model.CommandType;
import org.hofftech.edu.model.ParsedCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CommandParserTest {

    private CommandTypeService commandTypeServiceMock;
    private CommandParser commandParser;

    @BeforeEach
    void setUp() {
        commandTypeServiceMock = Mockito.mock(CommandTypeService.class);
        commandParser = new CommandParser(commandTypeServiceMock);
    }

    @Test
    void shouldParseValidCommand() {
        String command = "/load +name \"package1\" +form \"***,***\"";
        Mockito.when(commandTypeServiceMock.determineCommandType("LOAD"))
                .thenReturn(CommandType.LOAD);

        ParsedCommand parsedCommand = commandParser.parse(command);

        Assertions.assertThat(parsedCommand).isNotNull();
        Assertions.assertThat(parsedCommand.getCommandType()).isEqualTo(CommandType.LOAD);
        Assertions.assertThat(parsedCommand.getName()).isEqualTo("package1");
        Assertions.assertThat(parsedCommand.getForm()).isEqualTo("***,***");
    }

    @Test
    void shouldThrowExceptionForInvalidCommandType() {
        String command = "/invalid +name \"package1\"";
        Mockito.when(commandTypeServiceMock.determineCommandType("INVALID"))
                .thenThrow(new IllegalArgumentException("Неизвестная команда: INVALID"));

        Assertions.assertThatThrownBy(() -> commandParser.parse(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Неизвестная команда: INVALID");
    }

    @Test
    void shouldExtractParametersCorrectly() {
        String command = "+name \"package1\" +form \"***,***\"";

        ParsedCommand parsedCommand = commandParser.parse(command);

        Assertions.assertThat(parsedCommand).isNotNull();
        Assertions.assertThat(parsedCommand.getName()).isEqualTo("package1");
        Assertions.assertThat(parsedCommand.getForm()).isEqualTo("***,***");
    }
}

