package com.example.ecommercial.service.user;

import com.example.ecommercial.dao.UserDao;
import com.example.ecommercial.domain.dto.request.UserUpdateRequest;
import com.example.ecommercial.domain.dto.response.BaseResponse;
import com.example.ecommercial.domain.dto.response.UserGetResponse;
import com.example.ecommercial.domain.entity.UserEntity;
import com.example.ecommercial.domain.dto.request.UserCreateRequest;
import com.example.ecommercial.domain.enums.UserRole;
import com.example.ecommercial.domain.enums.UserState;
import com.example.ecommercial.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements BaseService<
        UserCreateRequest,
        BaseResponse,
        UserUpdateRequest> {

    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public BaseResponse save(UserCreateRequest userCreateRequest){
        UserEntity userEntity = modelMapper.map(userCreateRequest,UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        try {
            userDao.save(userEntity);
        }catch (Exception e){
            return BaseResponse.builder()
                    .message(userCreateRequest.getUsername()+" already exists")
                    .status(401)
                    .build();
        }
        return BaseResponse.builder()
                .status(200)
                .message(userCreateRequest.getUsername()+" successfully added")
                .build();
    }

    @Override
    public BaseResponse update(UserUpdateRequest userUpdateRequest) {
        Long userId = userUpdateRequest.getId();
        UserEntity userEntity = userDao.findById(userId).get();
        modelMapper.map(userUpdateRequest, userEntity);
        try {
            userDao.save(userEntity);
        } catch (Exception e) {
            return BaseResponse.builder()
                    .message(userEntity.getUsername()+" already exists")
                    .status(401)
                    .build();
        }
        return BaseResponse.builder()
                .status(200)
                .message("updated")
                .build();
    }

    @Override
    public BaseResponse delete(Long id) {
        userDao.deleteById(id);
        return BaseResponse.builder()
                .status(200)
                .message("deleted")
                .build();
    }

    @Override
    public BaseResponse getById(Long id) {
        return null;
    }

    @Override
    public BaseResponse<List<UserGetResponse>> getALl() {
        List<UserEntity> userEntities = userDao.findAll();
        return BaseResponse.<List<UserGetResponse>>builder()
                .status(200)
                .message("success")
                .data(modelMapper.map(userEntities,
                        new TypeToken<List<UserGetResponse>>(){}.getType()))
                .build();
    }

    public BaseResponse<List<UserGetResponse>> getAllBotUsers(){
        List<UserEntity> botUsers = userDao.findAll()
                .stream()
                .filter(userEntity -> userEntity.getUserRoles().contains(UserRole.USER))
                .toList();
        return BaseResponse.<List<UserGetResponse>>builder()
                .status(200)
                .message("success")
                .data(
                        modelMapper.map(botUsers,
                                new TypeToken<List<UserGetResponse>>(){}.getType())
                )
                .build();
    }

    public BaseResponse<UserState> getUserState(Long chatId) {
        Optional<UserEntity> optionalUser = userDao.findUserEntitiesByChatId(chatId);
        if (optionalUser.isPresent()){
            return BaseResponse.<UserState>builder()
                    .data(optionalUser.get().getUserState())
                    .status(200)
                    .build();
        }
        return BaseResponse.<UserState>builder()
                .status(404)
                .build();
    }

    public void saveBotUser(Long chatId, User user) {
        UserEntity userEntity = UserEntity.builder()
                .name(user.getUserName())
                .username(user.getUserName())
                .userState(UserState.REGISTERED)
                .chatId(chatId)
                .build();
        userDao.save(userEntity);
    }

    public void updateState(Long userId, UserState userState) {
        userDao.updateUserStateByChatId(userId, userState);
    }
}
