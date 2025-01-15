package org.hofftech.edu.factory.commandprocessor.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.ValidatorService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class UpdateCommandProcessor implements CommandProcessor {
    private final PackageRepository repository;
    private String result;

    @Override
    public void execute(ParsedCommand command) {
        String currentName = command.getOldName();
        Optional<Package> existingPackage = repository.findPackage(currentName);

        if (existingPackage.isEmpty()) {
            throw new RuntimeException("Ошибка: Посылка с именем '" + currentName + "' не найдена.");
        }

        try {
            String newName = command.getName() != null ? command.getName() : existingPackage.get().getName();
            boolean nameChanged = !newName.equals(existingPackage.get().getName());

            if (command.getSymbol() != null) {
                char newSymbol = command.getSymbol().charAt(0);
                if (newSymbol != existingPackage.get().getSymbol()) {
                    existingPackage.get().updateSymbol(newSymbol);
                }
            }

            List<String> newShape = command.getForm() != null
                    ? ValidatorService.validateFord(command.getForm())
                    : existingPackage.get().getShape();

            Package updatedPackage = new Package(newName, newShape, existingPackage.get().getSymbol(), existingPackage.get().getPackageStartPosition());

            if (nameChanged) {
                repository.deletePackage(currentName);
                repository.addPackage(updatedPackage);
            } else {
                repository.editPackage(currentName, updatedPackage);
            }

            StringBuilder output = new StringBuilder("Обновлённая посылка:\n");
            newShape.forEach(shape -> output.append(shape).append("\n"));
            result = output.toString();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Ошибка обновления посылки: " + e.getMessage());
        }
    }
}



