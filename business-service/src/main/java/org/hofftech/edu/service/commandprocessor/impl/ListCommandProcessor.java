package org.hofftech.edu.service.commandprocessor.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.mapper.PackageMapper;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListCommandProcessor implements CommandProcessor {

    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;

    @Override
    public Object execute(ParsedCommand command) {
        int page = command.getPage() != null ? command.getPage() : 0;
        int size = command.getSize() != null ? command.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        List<Package> packages = packageRepository.getAllPackages(pageable);
        if (packages.isEmpty()) {
            throw new PackageNotFoundException("Посылки не найдены");
        } else {
            return packages.stream()
                    .map(packageMapper::toDto)
                    .toList();
        }
    }

}
