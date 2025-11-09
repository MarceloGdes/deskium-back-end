package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import br.deskiumcompany.deskium_ai_api.dto.substatus.SubStatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("sub-status")
public class SubStatusController {

    @GetMapping
    private ResponseEntity<List<SubStatusDTO>> getAll(){
        List<SubStatusDTO> subStatusList = new ArrayList<>();
        for(SubStatus sb : SubStatus.values()){
            if(!sb.equals(SubStatus.FECHADO)){
                subStatusList.add(new SubStatusDTO(sb));
            }
        }
        return ResponseEntity.ok(subStatusList);
    }
}
