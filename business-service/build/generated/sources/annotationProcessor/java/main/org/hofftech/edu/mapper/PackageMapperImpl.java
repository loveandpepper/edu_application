package org.hofftech.edu.mapper;

import java.util.Arrays;
import javax.annotation.processing.Generated;
import org.hofftech.edu.model.CommandType;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.model.dto.PackageDto;
import org.hofftech.edu.model.dto.requestdto.BillingDto;
import org.hofftech.edu.model.dto.requestdto.CreatePackageDto;
import org.hofftech.edu.model.dto.requestdto.LoadPackageDto;
import org.hofftech.edu.model.dto.requestdto.UnloadPackageDto;
import org.hofftech.edu.model.dto.requestdto.UpdatePackageDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-31T18:47:32+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.jar, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class PackageMapperImpl implements PackageMapper {

    @Override
    public PackageDto toDto(Package entity) {
        if ( entity == null ) {
            return null;
        }

        PackageDto packageDto = new PackageDto();

        packageDto.setName( entity.getName() );
        String[] shape = entity.getShape();
        if ( shape != null ) {
            packageDto.setShape( Arrays.copyOf( shape, shape.length ) );
        }
        packageDto.setSymbol( entity.getSymbol() );
        packageDto.setStartPositionX( entity.getStartPositionX() );
        packageDto.setStartPositionY( entity.getStartPositionY() );

        return packageDto;
    }

    @Override
    public ParsedCommand toParsedCommand(CreatePackageDto dto) {
        if ( dto == null ) {
            return null;
        }

        ParsedCommand parsedCommand = new ParsedCommand();

        parsedCommand.setName( dto.getName() );
        parsedCommand.setForm( dto.getForm() );
        parsedCommand.setSymbol( dto.getSymbol() );

        parsedCommand.setCommandType( CommandType.CREATE );

        return parsedCommand;
    }

    @Override
    public ParsedCommand toParsedCommand(UpdatePackageDto dto) {
        if ( dto == null ) {
            return null;
        }

        ParsedCommand parsedCommand = new ParsedCommand();

        parsedCommand.setName( dto.getName() );
        parsedCommand.setOldName( dto.getOldName() );
        parsedCommand.setForm( dto.getForm() );
        parsedCommand.setSymbol( dto.getSymbol() );

        parsedCommand.setCommandType( CommandType.UPDATE );

        return parsedCommand;
    }

    @Override
    public ParsedCommand toParsedCommand(LoadPackageDto dto) {
        if ( dto == null ) {
            return null;
        }

        ParsedCommand parsedCommand = new ParsedCommand();

        parsedCommand.setSave( dto.isSave() );
        parsedCommand.setEasy( dto.isEasy() );
        parsedCommand.setEven( dto.isEven() );
        parsedCommand.setUser( dto.getUser() );
        parsedCommand.setParcelsText( dto.getParcelsText() );
        parsedCommand.setParcelsFile( dto.getParcelsFile() );
        parsedCommand.setTrucks( dto.getTrucks() );

        parsedCommand.setCommandType( CommandType.LOAD );

        return parsedCommand;
    }

    @Override
    public ParsedCommand toParsedCommand(UnloadPackageDto dto) {
        if ( dto == null ) {
            return null;
        }

        ParsedCommand parsedCommand = new ParsedCommand();

        parsedCommand.setUser( dto.getUser() );
        parsedCommand.setInFile( dto.getInFile() );
        parsedCommand.setWithCount( dto.isWithCount() );

        parsedCommand.setCommandType( CommandType.UNLOAD );

        return parsedCommand;
    }

    @Override
    public ParsedCommand toParsedCommand(BillingDto dto) {
        if ( dto == null ) {
            return null;
        }

        ParsedCommand parsedCommand = new ParsedCommand();

        parsedCommand.setUser( dto.getUser() );
        parsedCommand.setFrom( dto.getFrom() );
        parsedCommand.setTo( dto.getTo() );

        parsedCommand.setCommandType( CommandType.BILLING );

        return parsedCommand;
    }
}
