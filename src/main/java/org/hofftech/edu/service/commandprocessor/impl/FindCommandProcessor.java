package org.hofftech.edu.service.commandprocessor.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.exception.PackageNameException;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.mapper.PackageMapper;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

@RequiredArgsConstructor
public class FindCommandProcessor implements CommandProcessor {

    private final PackageRepository packageRepository;

    @Override
    public Object execute(ParsedCommand command) {
        String name = command.getName();
        if (name == null || name.isEmpty()) {
            throw new PackageNameException("Имя посылки не указано");
        }

        return packageRepository.findPackage(name)
                .map(PackageMapper.INSTANCE::toDto)
                .orElseThrow(() -> new PackageNotFoundException(
                        "Посылка с именем '" + name + "' не найдена."
                ));
    }
}
