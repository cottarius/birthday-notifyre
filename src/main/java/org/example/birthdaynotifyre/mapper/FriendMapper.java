package org.example.birthdaynotifyre.mapper;

import org.example.birthdaynotifyre.dto.friend.FriendDto;
import org.example.birthdaynotifyre.entity.Friend;
import org.example.birthdaynotifyre.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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
    @Mapping(target = "user", source = "userId", qualifiedByName = "userIdToUser")
    Friend toEntity(FriendDto friendDto);
    
    /**
     * Преобразует userId в объект User.
     *
     * @param userId идентификатор пользователя
     * @return объект User
     */
    @Named("userIdToUser")
    default User userIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

    /**
     * Преобразует сущность Friend в DTO FriendDto.
     *
     * @param friend сущность Friend
     * @return DTO сущности Friend
     */
    @Mapping(target = "userId", source = "user.id")
    FriendDto toDto(Friend friend);
}
