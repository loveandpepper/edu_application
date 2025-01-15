package org.hofftech.edu.factory.commandprocessor.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageStartPosition;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.ValidatorService;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class CreateCommandProcessor implements CommandProcessor {
    private static final int FIRST_CHAR_INDEX = 0;
    private final PackageRepository repository;
    private final ValidatorService validatorService;
    private String result;

    @Override
    public void execute(ParsedCommand command) {
        String name = command.getName();
        String form = command.getForm();
        char symbol = command.getSymbol() != null ? command.getSymbol().charAt(FIRST_CHAR_INDEX) : ' ';

        if (name == null || form == null) {
            throw new RuntimeException("Недостаточно данных для создания посылки");
        }

        List<String> shape = validatorService.validateFord(form);
        Package newPackage = new Package(name, shape, symbol, new PackageStartPosition(0, 0));
        repository.addPackage(newPackage);

        StringBuilder output = new StringBuilder("Создана посылка " + newPackage.getName() + "\nФорма посылки:\n");
        shape.forEach(line -> output.append(line).append("\n"));
        result = output.toString();
    }
}
