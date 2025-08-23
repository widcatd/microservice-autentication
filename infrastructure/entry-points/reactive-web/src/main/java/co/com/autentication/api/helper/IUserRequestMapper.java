package co.com.autentication.api.helper;

import co.com.autentication.api.dto.CreateUserDto;
import co.com.autentication.api.dto.UserDto;
import co.com.autentication.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserRequestMapper {
    User toModel(CreateUserDto createUserDto);
    User toModel(UserDto userDto);
}
