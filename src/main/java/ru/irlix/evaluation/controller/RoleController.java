package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.RoleRequest;
import ru.irlix.evaluation.dto.response.RoleResponse;
import ru.irlix.evaluation.service.RoleService;
import ru.irlix.evaluation.utils.UrlConstants;

import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public RoleResponse createRole(@RequestBody RoleRequest roleRequest) {
        return roleService.createRole(roleRequest);
    }

    @PutMapping("/{id}")
    public RoleResponse updateRole(@PathVariable("id") Long id,
                                   @RequestBody RoleRequest roleRequest) {
        return roleService.updateRole(id, roleRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
    }

    @GetMapping("/{id}")
    public RoleResponse findRoleById(@PathVariable("id") Long id) {
        return roleService.findRoleResponseById(id);
    }

    @GetMapping
    public List<RoleResponse> findAllRoles() {
        return roleService.findAllRoles();
    }
}
