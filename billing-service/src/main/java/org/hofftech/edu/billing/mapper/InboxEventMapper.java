package org.hofftech.edu.billing.mapper;

import org.hofftech.edu.billing.model.dto.InboxEventDto;
import org.hofftech.edu.billing.model.InboxEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Маппер для преобразования между InboxEventEntity и InboxEventDto.
 * Использует MapStruct для генерации реализации.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InboxEventMapper {

    /**
     * Преобразует DTO в сущность InboxEventEntity.
     *
     * @param dto объект InboxEventDto.
     * @return объект InboxEventEntity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receivedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "processed", constant = "false")
    InboxEventEntity toEntity(InboxEventDto dto);

    /**
     * Преобразует сущность InboxEventEntity в DTO.
     *
     * @param entity объект InboxEventEntity.
     * @return объект InboxEventDto.
     */
    InboxEventDto toDto(InboxEventEntity entity);
}
