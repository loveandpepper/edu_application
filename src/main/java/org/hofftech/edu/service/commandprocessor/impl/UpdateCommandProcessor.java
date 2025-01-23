package org.hofftech.edu.service.commandprocessor.impl;

import lombok.AllArgsConstructor;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.ValidatorService;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UpdateCommandProcessor implements CommandProcessor {

    private static final int INDEX_OF_FIRST_SYMBOL = 0;
    private final PackageRepository packageRepository;
    private final ValidatorService validatorService;

    @Override
    public String execute(ParsedCommand command) {
        String currentName = command.getOldName();
        Package existingPackage = packageRepository.findPackage(currentName)
                .orElseThrow(() -> new PackageNotFoundException("Посылка с именем '" + currentName + "' не найдена."));

        String newName = Optional.ofNullable(command.getName())
                .orElse(existingPackage.getName());

        char newSymbol = Optional.ofNullable(command.getSymbol())
                .map(symbol -> symbol.charAt(INDEX_OF_FIRST_SYMBOL))
                .orElse(existingPackage.getSymbol());

        if (newSymbol != existingPackage.getSymbol()) {
            existingPackage.updateSymbol(newSymbol);
        }

        List<String> newShape = Optional.ofNullable(command.getForm())
                .map(validatorService::validateForm)
                .orElse(existingPackage.getShape());

        Package updatedPackage = new Package(
                newName,
                newShape,
                existingPackage.getSymbol(),
                existingPackage.getPackageStartPosition()
        );

        packageRepository.editPackage(currentName, updatedPackage);

        StringBuilder output = new StringBuilder("Обновлённая посылка:\n");
        newShape.forEach(shape -> output.append(shape).append('\n'));
        return output.toString();
    }
}



