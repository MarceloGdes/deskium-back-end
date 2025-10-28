package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.dto.status.StatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("status")
public class StatusController {

    @GetMapping
    private ResponseEntity<List<StatusDTO>> getAll(){
        List<StatusDTO> statusList = new ArrayList<>();
        for(Status s : Status.values()){
            statusList.add(new StatusDTO(s));
        }
        return ResponseEntity.ok(statusList);
    }
}
