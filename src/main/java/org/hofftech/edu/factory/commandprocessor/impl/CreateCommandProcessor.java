package org.hofftech.edu.factory.commandprocessor.impl;

import org.hofftech.edu.exception.PackageArgumentException;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageStartPosition;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.ValidatorService;

import java.util.List;

public record CreateCommandProcessor(PackageRepository packageRepository,
                                     ValidatorService validatorService) implements CommandProcessor {

    private static final int FIRST_CHAR_INDEX = 0;
    private static final int POSITION_START_INDEX = 0;

    @Override
    public String execute(ParsedCommand command) {
        String name = command.getName();
        String form = command.getForm();
        char symbol = command.getSymbol() != null ? command.getSymbol().charAt(FIRST_CHAR_INDEX) : ' ';

        if (name == null || form == null) {
            throw new PackageArgumentException("Недостаточно данных для создания посылки");
        }

        List<String> shape = validatorService.validateForm(form);
        Package newPackage = new Package(name, shape, symbol, new PackageStartPosition(POSITION_START_INDEX, POSITION_START_INDEX));
        packageRepository.addPackage(newPackage);

        StringBuilder output = new StringBuilder("Создана посылка " + newPackage.getName() + "\nФорма посылки:\n");
        shape.forEach(line -> output.append(line).append("\n"));
        return output.toString();
    }
}
