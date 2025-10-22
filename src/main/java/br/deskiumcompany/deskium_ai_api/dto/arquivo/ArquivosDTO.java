package br.deskiumcompany.deskium_ai_api.dto.arquivo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArquivosDTO {

    private List<String> fileNames;
}
