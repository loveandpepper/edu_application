package org.hofftech.edu.mapper;

import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.model.dto.PackageDto;
import org.hofftech.edu.model.dto.requestdto.CreatePackageDto;
import org.hofftech.edu.model.dto.requestdto.LoadPackageDto;
import org.hofftech.edu.model.dto.requestdto.UnloadPackageDto;
import org.hofftech.edu.model.dto.requestdto.UpdatePackageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Маппер для преобразования между Package и PackageDto.
 * Использует MapStruct для генерации реализации.
 */
@Mapper
public interface PackageMapper {

    PackageMapper INSTANCE = Mappers.getMapper(PackageMapper.class);

    /**
     * Преобразует сущность Package в DTO.
     *
     * @param entity объект Package.
     * @return объект PackageDto.
     */
    PackageDto toDto(Package entity);

    /**
     * Преобразует DTO в сущность Package.
     *
     * @param dto объект PackageDto.
     * @return объект Package.
     */

    /**
     * Преобразует CreatePackageDto в ParsedCommand.
     *
     * @param dto объект CreatePackageDto.
     * @return объект ParsedCommand.
     */
    @Mapping(target = "commandType", constant = "CREATE")
    ParsedCommand toParsedCommand(CreatePackageDto dto);

    /**
     * Преобразует UpdatePackageDto в ParsedCommand.
     *
     * @param dto объект UpdatePackageDto.
     * @return объект ParsedCommand.
     */
    @Mapping(target = "commandType", constant = "UPDATE")
    ParsedCommand toParsedCommand(UpdatePackageDto dto);

    /**
     * Преобразует LoadPackagesDto в ParsedCommand.
     *
     * @param dto объект LoadPackagesDto.
     * @return объект ParsedCommand.
     */
    @Mapping(target = "commandType", constant = "LOAD")
    ParsedCommand toParsedCommand(LoadPackageDto dto);

    /**
     * Преобразует UnloadPackagesDto в ParsedCommand.
     *
     * @param dto объект UnloadPackagesDto.
     * @return объект ParsedCommand.
     */
    @Mapping(target = "commandType", constant = "UNLOAD")
    ParsedCommand toParsedCommand(UnloadPackageDto dto);

}



