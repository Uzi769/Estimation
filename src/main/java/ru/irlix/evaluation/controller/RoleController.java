package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.RoleRequest;
import ru.irlix.evaluation.dto.response.RoleResponse;
import ru.irlix.evaluation.service.RoleService;
import ru.irlix.evaluation.utils.constant.UrlConstants;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/roles")
@RequiredArgsConstructor
@CrossOrigin
@Log4j2
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public RoleResponse createRole(@RequestBody RoleRequest roleRequest) {
        log.info(UrlConstants.RECEIVED_ENTITY);
        return roleService.createRole(roleRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public RoleResponse updateRole(@PathVariable("id") @Positive(message = "{id.positive}") Long id,
                                   @RequestBody RoleRequest roleRequest) {
        log.info(UrlConstants.RECEIVED_ENTITY_ID + id);
        return roleService.updateRole(id, roleRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        roleService.deleteRole(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SALES')")
    @GetMapping("/{id}")
    public RoleResponse findRoleById(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        return roleService.findRoleResponseById(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SALES')")
    @GetMapping
    public List<RoleResponse> findAllRoles() {
        log.info(UrlConstants.RECEIVED_NO_ARGS);
        return roleService.findAllRoles();
    }
}
