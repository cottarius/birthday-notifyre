package org.example.birthdaynotifyre.mapper;

import org.example.birthdaynotifyre.dto.FriendDto;
import org.example.birthdaynotifyre.entity.Friend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для работы с сущностью Friend.
 */
@Mapper(componentModel = "spring")
public interface FriendMapper {

    /**
     * Преобразует DTO FriendDto в сущность Friend.
     *
     * @param friendDto DTO сущности Friend
     * @return сущность Friend
     */
    @Mapping(target = "id",  ignore = true)
    Friend toEntity(FriendDto friendDto);

    /**
     * Преобразует сущность Friend в DTO FriendDto.
     *
     * @param friend сущность Friend
     * @return DTO сущности Friend
     */
    FriendDto toDto(Friend friend);
}
