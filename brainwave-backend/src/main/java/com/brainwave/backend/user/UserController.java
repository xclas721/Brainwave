package com.brainwave.backend.user;

import com.brainwave.core.common.BaseController;
import com.brainwave.core.common.Result;
import com.brainwave.service.user.converter.UserConverter;
import com.brainwave.service.user.dto.UserDto;
import com.brainwave.service.user.request.UserRequest;
import com.brainwave.service.user.service.UserService;
import com.brainwave.service.user.vo.UserVo;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用者 Controller - 基於 BaseController 的 RESTful API
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;
    private final UserConverter userConverter;

    /**
     * 新增使用者
     */
    @PostMapping
    public ResponseEntity<Result<UserDto>> createUser(@Valid @RequestBody UserRequest request) {
        UserDto userDto = userService.createUser(request);
        return success("使用者新增成功", userDto);
    }

    /**
     * 根據 ID 取得使用者
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result<UserVo>> getUser(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        // 轉換為 VO
        UserVo userVo = userConverter.toVoFromDto(userDto);
        return success(userVo);
    }

    /**
     * 取得所有使用者
     */
    @GetMapping
    public ResponseEntity<Result<List<UserVo>>> getAllUsers() {
        List<UserVo> users = userService.getAllUsers();
        return success(users);
    }

    /**
     * 更新使用者
     */
    @PutMapping("/{id}")
    public ResponseEntity<Result<UserDto>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        UserDto userDto = userService.updateUser(id, request);
        return success("使用者更新成功", userDto);
    }

    /**
     * 刪除使用者
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return success("使用者刪除成功", null);
    }
}
