package com.brainwave.backend.member;

import com.brainwave.core.common.BaseController;
import com.brainwave.core.common.PageResponse;
import com.brainwave.core.common.Result;
import com.brainwave.service.member.converter.MemberConverter;
import com.brainwave.service.member.dto.MemberDto;
import com.brainwave.service.member.request.MemberRequest;
import com.brainwave.service.member.request.MemberSearchRequest;
import com.brainwave.service.member.service.MemberService;
import com.brainwave.service.member.vo.MemberVo;
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
 * 會員 Controller - 管理前台會員
 */
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController extends BaseController {

    private final MemberService memberService;
    private final MemberConverter memberConverter;

    @PostMapping
    public ResponseEntity<Result<MemberDto>> createMember(@Valid @RequestBody MemberRequest request) {
        MemberDto dto = memberService.createMember(request);
        return success("會員新增成功", dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<MemberVo>> getMember(@PathVariable Long id) {
        MemberDto dto = memberService.getMemberById(id);
        MemberVo vo = memberConverter.toVoFromDto(dto);
        return success(vo);
    }

    @GetMapping
    public ResponseEntity<Result<List<MemberVo>>> getAllMembers() {
        List<MemberVo> list = memberService.getAllMembers();
        return success(list);
    }

    @GetMapping("/search")
    public ResponseEntity<Result<PageResponse<MemberVo>>> searchMembers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        MemberSearchRequest searchRequest = MemberSearchRequest.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        if (direction == null) {
            direction = Sort.Direction.ASC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<MemberVo> pageResult = memberService.searchMembers(searchRequest, pageable);
        return successPage(pageResult);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<MemberDto>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {
        MemberDto dto = memberService.updateMember(id, request);
        return success("會員更新成功", dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return success("會員刪除成功", null);
    }
}
