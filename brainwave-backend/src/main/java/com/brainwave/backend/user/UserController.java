package com.brainwave.backend.user;

import com.brainwave.core.common.BaseController;
import com.brainwave.core.common.PageResponse;
import com.brainwave.core.common.Result;
import com.brainwave.service.user.converter.UserConverter;
import com.brainwave.service.user.dto.UserDto;
import com.brainwave.service.user.request.UserRequest;
import com.brainwave.service.user.request.UserSearchRequest;
import com.brainwave.service.user.request.UserUpdateRequest;
import com.brainwave.service.user.service.UserService;
import com.brainwave.service.user.vo.UserVo;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * 取得所有使用者（無分頁，僅用於少量資料）
     */
    @GetMapping
    public ResponseEntity<Result<List<UserVo>>> getAllUsers() {
        List<UserVo> users = userService.getAllUsers();
        return success(users);
    }

    /**
     * 分頁查詢使用者（支援條件查詢和排序）
     * 使用 GET 方法，所有參數通過 Query Parameters 傳遞
     * 範例：GET /api/users/search?page=0&size=10&sortBy=name&direction=ASC&name=張三&email=test
     */
    @GetMapping("/search")
    public ResponseEntity<Result<PageResponse<UserVo>>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        // 建立查詢條件
        UserSearchRequest searchRequest = UserSearchRequest.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        // 建立分頁和排序（滿足 null 安全：RequestParam 在缺少參數時可能為 null）
        if (direction == null) {
            direction = Sort.Direction.ASC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // 執行查詢並自動轉換為 PageResponse
        Page<UserVo> pageResult = userService.searchUsers(searchRequest, pageable);
        return successPage(pageResult);
    }

    /**
     * 更新使用者（僅 profile：username, name, email, phone；不含密碼）
     */
    @PutMapping("/{id}")
    public ResponseEntity<Result<UserDto>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
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
