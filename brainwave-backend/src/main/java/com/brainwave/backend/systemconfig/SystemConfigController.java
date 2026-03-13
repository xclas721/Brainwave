package com.brainwave.backend.systemconfig;

import com.brainwave.core.common.BaseController;
import com.brainwave.core.common.PageResponse;
import com.brainwave.core.common.Result;
import com.brainwave.service.systemconfig.dto.SystemConfigDto;
import com.brainwave.service.systemconfig.request.SystemConfigRequest;
import com.brainwave.service.systemconfig.request.SystemConfigSearchRequest;
import com.brainwave.service.systemconfig.request.SystemConfigUpdateRequest;
import com.brainwave.service.systemconfig.service.SystemConfigService;
import com.brainwave.service.systemconfig.vo.SystemConfigVo;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * System Config 管理 API
 */
@RestController
@RequestMapping("/api/system-configs")
@RequiredArgsConstructor
public class SystemConfigController extends BaseController {

    private final SystemConfigService systemConfigService;

    /**
     * 依 key 取得設定
     */
    @GetMapping("/by-key/{key}")
    public ResponseEntity<Result<SystemConfigDto>> getByKey(@PathVariable String key) {
        SystemConfigDto dto = systemConfigService.getByKey(key);
        return success(dto);
    }

    /**
     * 依 id 取得設定（管理後台查看用）
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result<SystemConfigVo>> getById(@PathVariable Long id) {
        SystemConfigVo vo = systemConfigService.getById(id);
        return success(vo);
    }

    /**
     * 取得所有設定（筆數少時使用）
     */
    @GetMapping
    public ResponseEntity<Result<List<SystemConfigVo>>> getAll() {
        List<SystemConfigVo> list = systemConfigService.findAllList();
        return success(list);
    }

    /**
     * 分頁查詢設定
     */
    @GetMapping("/search")
    public ResponseEntity<Result<PageResponse<SystemConfigVo>>> search(
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        if (direction == null) {
            direction = Sort.Direction.ASC;
        }

        SystemConfigSearchRequest request = SystemConfigSearchRequest.builder()
                .key(key)
                .type(type)
                .build();

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SystemConfigVo> pageResult = systemConfigService.search(request, pageable);
        return successPage(pageResult);
    }

    /**
     * 新增設定（若 key 已存在可改用 upsert）
     */
    @PostMapping
    public ResponseEntity<Result<SystemConfigDto>> create(@Valid @RequestBody SystemConfigRequest request) {
        SystemConfigDto dto = systemConfigService.create(request);
        return success("系統設定建立成功", dto);
    }

    /**
     * 以 key 為主鍵的 upsert，適合由部署/腳本重複呼叫
     */
    @PostMapping("/upsert")
    public ResponseEntity<Result<SystemConfigDto>> upsert(@Valid @RequestBody SystemConfigRequest request) {
        SystemConfigDto dto = systemConfigService.upsert(request);
        return success("系統設定已更新", dto);
    }

    /**
     * 更新指定 id 的設定內容
     */
    @PutMapping("/{id}")
    public ResponseEntity<Result<SystemConfigDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody SystemConfigUpdateRequest request) {
        SystemConfigDto dto = systemConfigService.update(id, request);
        return success("系統設定更新成功", dto);
    }
}

