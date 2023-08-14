package com.example.common.service;

import com.example.common.exception.NullEntityReferenceException;
import com.example.common.model.Role;
import com.example.common.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RoleServiceImplTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateRole_ValidRole_Success() {
        Role role = new Role();
        role.setName("ADMIN");

        Role createdRole = roleService.create(role);

        assertNotNull(createdRole);
        assertNotNull(createdRole.getId());
        assertEquals(role.getName(), createdRole.getName());
    }

    @Test
    public void testCreateRole_NullRole_ThrowsNullEntityReferenceException() {
        assertThrows(NullEntityReferenceException.class, () -> roleService.create(null));
    }

    @Test
    public void testReadRoleById_ExistingRoleId_Success() {
        Role role = new Role();
        role.setName("ADMIN");
        Role savedRole = roleRepository.save(role);

        Role foundRole = roleService.readById(savedRole.getId());

        assertNotNull(foundRole);
        assertEquals(savedRole.getId(), foundRole.getId());
        assertEquals(savedRole.getName(), foundRole.getName());
    }

    @Test
    public void testReadRoleById_NonExistingRoleId_ThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> roleService.readById(999L));
    }

    @Test
    public void testUpdateRole_ValidRole_Success() {
        Role role = new Role();
        role.setName("ADMIN");
        Role savedRole = roleRepository.save(role);

        Role updatedRole = new Role();
        updatedRole.setId(savedRole.getId());
        updatedRole.setName("SUPERADMIN");

        Role returnedRole = roleService.update(updatedRole);

        assertNotNull(returnedRole);
        assertEquals(savedRole.getId(), returnedRole.getId());
        assertEquals(updatedRole.getName(), returnedRole.getName());
    }

    @Test
    public void testUpdateRole_NullRole_ThrowsNullEntityReferenceException() {
        assertThrows(NullEntityReferenceException.class, () -> roleService.update(null));
    }

    @Test
    public void testUpdateRole_NonExistingRoleId_ThrowsEntityNotFoundException() {
        Role role = new Role();
        role.setId(999L);
        role.setName("ADMIN");

        assertThrows(EntityNotFoundException.class, () -> roleService.update(role));
    }

    @Test
    public void testDeleteRole_ExistingRoleId_Success() {
        Role role = new Role();
        role.setName("ADMIN");
        Role savedRole = roleRepository.save(role);

        roleService.delete(savedRole.getId());

        assertFalse(roleRepository.existsById(savedRole.getId()));
    }

    @Test
    public void testDeleteRole_NonExistingRoleId_ThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> roleService.delete(999L));
    }

    @Test
    public void testGetAllRoles_NoRolesInDatabase_ReturnsEmptyList() {
        List<Role> roles = roleService.getAll();

        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    @Test
    public void testGetAllRoles_RolesInDatabase_ReturnsListOfRoles() {
        Role role1 = new Role();
        role1.setName("ADMIN");
        Role savedRole1 = roleRepository.save(role1);

        Role role2 = new Role();
        role2.setName("USER");
        Role savedRole2 = roleRepository.save(role2);

        List<Role> roles = roleService.getAll();

        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(savedRole1));
        assertTrue(roles.contains(savedRole2));
    }

    @Test
    public void testReadRoleByName_ExistingRoleName_Success() {
        Role role = new Role();
        role.setName("ADMIN");
        Role savedRole = roleRepository.save(role);

        Role foundRole = roleService.readByName("ADMIN");

        assertNotNull(foundRole);
        assertEquals(savedRole.getId(), foundRole.getId());
        assertEquals(savedRole.getName(), foundRole.getName());
    }

    @Test
    public void testReadRoleByName_NonExistingRoleName_ThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> roleService.readByName("NONEXISTING"));
    }
}
