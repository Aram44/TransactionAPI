package com.example.transactionapi.services.implementation;

import com.example.transactionapi.models.Role;
import com.example.transactionapi.repository.RoleRepository;
import com.example.transactionapi.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImplementation implements ShowService<Role> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role findById(Integer id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public Role saveOrUpdate(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public String deleteById(Integer id) {
        JSONObject jsonObject = new JSONObject();
        try {
            roleRepository.deleteById(id);
            jsonObject.put("message", "Role deleted successfully");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
