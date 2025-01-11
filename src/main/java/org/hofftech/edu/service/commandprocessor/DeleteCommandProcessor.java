package org.hofftech.edu.service.commandprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;

@Slf4j
@RequiredArgsConstructor
public class DeleteCommandProcessor implements CommandProcessor {
    private final PackageRepository repository;

    @Override
    public void execute(ParsedCommand command) {
        String name = command.getName();
        repository.deletePackage(name);

        log.info("Посылка '{}' успешно удалена.", name);
    }
}
