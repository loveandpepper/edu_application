package org.hofftech.edu.service.commandprocessor.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.ValidatorService;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class UpdateCommandProcessor implements CommandProcessor {

    private static final int INDEX_OF_FIRST_SYMBOL = 0;
    private final PackageRepository packageRepository;
    private final ValidatorService validatorService;

    @Transactional
    @Override
    public String execute(ParsedCommand command) {
        String currentName = command.getOldName();
        Package existingPackage = packageRepository.findById(currentName)
                .orElseThrow(() -> new PackageNotFoundException(
                        "Посылка с именем '" + currentName + "' не найдена."
                ));

        String oldName = existingPackage.getName();
        char oldSymbol = existingPackage.getSymbol();
        String[] oldShape = existingPackage.getShape();

        String newName = Optional.ofNullable(command.getName())
                .orElse(oldName);

        char newSymbol = Optional.ofNullable(command.getSymbol())
                .map(s -> s.charAt(INDEX_OF_FIRST_SYMBOL))
                .orElse(oldSymbol);

        String[] newShape = Optional.ofNullable(command.getForm())
                .map(validatorService::validateForm)
                .orElse(oldShape);

        if (newSymbol != oldSymbol) {
            newShape = Arrays.stream(newShape)
                    .map(line -> line.replace(oldSymbol, newSymbol))
                    .toArray(String[]::new);
        }

        Package updatedPackage = new Package(
                newName,
                newShape,
                newSymbol,
                existingPackage.getStartPositionX(),
                existingPackage.getStartPositionY()
        );

        packageRepository.deleteById(oldName);
        packageRepository.save(updatedPackage);

        StringBuilder output = new StringBuilder("Обновлённая посылка: ")
                .append(updatedPackage.getName()).append("\n");

        Arrays.stream(updatedPackage.getShape())
                .forEach(shapeLine -> output.append(shapeLine).append("\n"));

        return output.toString();
    }

}
