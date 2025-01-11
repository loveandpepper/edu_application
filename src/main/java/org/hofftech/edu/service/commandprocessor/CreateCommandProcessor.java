package org.hofftech.edu.service.commandprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageStartPosition;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.ValidatorService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CreateCommandProcessor implements CommandProcessor {
    private final PackageRepository repository;

    @Override
    public void execute(ParsedCommand command) {
        String name = command.getName();
        String form = command.getForm();
        char symbol = command.getSymbol() != null ? command.getSymbol().charAt(0) : ' ';

        if (name == null || form == null) {
            log.error("Недостаточно данных для создания посылки.");
            return;
        }

        List<String> shape = ValidatorService.isAbleToParseForm(form);
        Package newPackage = new Package(name, shape, symbol, new PackageStartPosition(0, 0));
        repository.addPackage(newPackage);

        log.info("Посылка '{}' успешно создана.", name);

        StringBuilder output = new StringBuilder();
        output.append("Форма посылки:\n");
        shape.forEach(line -> output.append(line).append("\n"));

        System.out.println(output.toString());
    }
}
