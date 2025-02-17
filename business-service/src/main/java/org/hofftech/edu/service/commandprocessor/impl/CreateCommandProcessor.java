package org.hofftech.edu.service.commandprocessor.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.exception.PackageArgumentException;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.ValidatorService;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

import java.util.Arrays;

@RequiredArgsConstructor
public class CreateCommandProcessor implements CommandProcessor {

    private final PackageRepository packageRepository;
    private final ValidatorService validatorService;

    private static final int FIRST_CHAR_INDEX = 0;

    @Override
    public String execute(ParsedCommand command) {
        String name = command.getName();
        String form = command.getForm();
        char symbol = (command.getSymbol() != null)
                ? command.getSymbol().charAt(FIRST_CHAR_INDEX)
                : ' ';

        if (name == null || form == null) {
            throw new PackageArgumentException("Недостаточно данных для создания посылки");
        }

        String[] shape = validatorService.validateForm(form);

        Package newPackage = new Package(
                name, shape, symbol, 0, 0
        );
        packageRepository.save(newPackage);

        StringBuilder output = new StringBuilder("Создана посылка: ")
                .append(newPackage.getName())
                .append("\nФорма посылки:\n");

        Arrays.stream(shape).forEach(line -> output.append(line).append("\n"));

        return output.toString();
    }
}
