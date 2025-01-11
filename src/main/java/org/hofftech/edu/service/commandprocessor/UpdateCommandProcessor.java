package org.hofftech.edu.service.commandprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.ValidatorService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class UpdateCommandProcessor implements CommandProcessor {
    private final PackageRepository repository;

    @Override
    public void execute(ParsedCommand command) {
        String currentName = command.getOldName();
        Package existingPackage = repository.findPackage(currentName);

        if (existingPackage == null) {
            log.error("Посылка с именем '{}' не найдена.", currentName);
            return;
        }

        try {
            String newName = command.getName() != null ? command.getName() : existingPackage.getName();
            boolean nameChanged = !newName.equals(existingPackage.getName());

            if (command.getSymbol() != null) {
                char newSymbol = command.getSymbol().charAt(0);
                if (newSymbol != existingPackage.getSymbol()) {
                    existingPackage.updateSymbol(newSymbol);
                }
            }

            List<String> newShape = command.getForm() != null ? ValidatorService.isAbleToParseForm(command.getForm())
                    : existingPackage.getShape();

            Package updatedPackage = new Package(newName, newShape, existingPackage.getSymbol(), existingPackage.getPackageStartPosition());

            if (nameChanged) {
                repository.deletePackage(currentName);
                repository.addPackage(updatedPackage);
            } else {
                repository.editPackage(currentName, updatedPackage);
            }

            log.info("Посылка '{}' успешно обновлена.", currentName);

            StringBuilder output = new StringBuilder();
            output.append("Обновлённая посылка:\n");
            updatedPackage.getShape().forEach(shape -> output.append(shape).append("\n"));
            System.out.println(output.toString());
        } catch (IllegalArgumentException e) {
            log.error("Ошибка обновления посылки: {}", e.getMessage());
        }
    }
}


