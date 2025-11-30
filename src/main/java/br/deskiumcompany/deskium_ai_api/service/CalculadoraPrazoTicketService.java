package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.*;
import br.deskiumcompany.deskium_ai_api.domain.enums.DiaSemana;
import br.deskiumcompany.deskium_ai_api.respository.ExpedienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class CalculadoraPrazoTicketService {

    @Autowired
    private ExpedienteRepository expedienteRepository;

    public void calcularPrazosPrimeiraRespostaEResolucao(Ticket ticket) {
        int horasPrimeiraResposta = calcularHoras(ticket, true);
        int horasResolucao = calcularHoras(ticket, false);

        LocalDateTime dtCriacaoTicket = ticket.getCriadoEm();

        ticket.setPrevisaoPrimeiraResposta(adicionarHorasUteis(dtCriacaoTicket, horasPrimeiraResposta));
        ticket.setPrevisaoResolucao(adicionarHorasUteis(dtCriacaoTicket, horasResolucao));
    }

    //Adiciona dias uteis, apra calculo de prazos.
    public LocalDateTime calcularPrazoReabertura(LocalDateTime data, int dias){
        for (int i = dias; i > 0; i--) {
            data = buscarProximaDataUtil(data);
            data = data.plusDays(1);
        }

        return data;
    }

    private int calcularHoras(Ticket ticket, boolean primeiraResposta) {
        Motivo motivo = ticket.getMotivo();
        Categoria categoria = ticket.getCategoria();
        Prioridade prioridade = ticket.getPrioridade();

        int horas = primeiraResposta ?
                motivo.getPrazoPrimeiraResposta() :
                motivo.getPrazoResolucao();

        if (categoria != null) {
            horas -= primeiraResposta ?
                    categoria.getDecressimoPrazoPrimeiraResposta() :
                    categoria.getDecressimoPrazoResolucao();
        }

        if (prioridade != null) {
            horas -= primeiraResposta ?
                    prioridade.getDecressimoPrazoPrimeiraResposta() :
                    prioridade.getDecressimoPrazoResolucao();
        }

        //Evitando horas negativas
        return Math.max(horas, 1);
    }

    private LocalDateTime adicionarHorasUteis(LocalDateTime dtCriacaoTicket, int horas) {
        LocalDateTime atual = dtCriacaoTicket;
        int horasRestantes = horas;

        while (horasRestantes > 0) {
            Expediente expediente = buscarExpediente(atual.getDayOfWeek());

            // Pula dias sem expediente
            if (expediente == null || !expediente.isExpediente()) {
                atual = buscarProximaDataUtil(atual.plusDays(1));
                continue;
            }

            // Ajusta para horário de expediente se necessário (casos onde está fora da jornada).
            //Começa do horário certo;
            //necessário validar se o horario é igual ao fim da manha, apra poder contabilizar as horas a partir do inicio da tarde.
            atual = ajustarHorario(atual, expediente, true);

            // Adiciona 1 hora e remove uma hora restante.
            atual = atual.plusHours(1);
            horasRestantes--;

            //Ajusta o horário novamente, caso tenha ficado fora de expediente, alterando para outro período do dia ou para ao próximo dia útil;
            atual = ajustarHorario(atual, expediente, false);
        }

        return atual;
    }

    private LocalDateTime ajustarHorario(LocalDateTime data, Expediente exp, boolean ajusteInicial) {
        LocalTime hora = data.toLocalTime();

        // se hora é antes do inicio do expediente
        if (hora.isBefore(exp.getInicioManha())) {
            return data.with(exp.getInicioManha());
        }

        // se hora é depois do fim da manhã e antes do inicio da tarde
        if ((hora.isAfter(exp.getFimManha())) && hora.isBefore(exp.getInicioTarde())) {
            return data.with(exp.getInicioTarde());
        }

        // se hora é um ajuste inicial e é igual ao fim da manhã e antes do inicio da tarde
        if (hora.equals(exp.getFimManha()) && ajusteInicial && hora.isBefore(exp.getInicioTarde())) {
            return data.with(exp.getInicioTarde());
        }

        //se a hora é após o fim da tarde
        if (hora.isAfter(exp.getFimTarde())) {
            return buscarProximaDataUtil(data.plusDays(1));
        }

        return data;
    }

    private LocalDateTime buscarProximaDataUtil(LocalDateTime data) {
        LocalDateTime proximoDia = data;

        while (true) {
            Expediente exp = buscarExpediente(proximoDia.getDayOfWeek());

            if (exp != null && exp.isExpediente()) {
                // Retorna com o horário de início do expediente deste dia
                return proximoDia.with(exp.getInicioManha());
            }

            proximoDia = proximoDia.plusDays(1);
        }
    }



    private Expediente buscarExpediente(DayOfWeek day) {
        return expedienteRepository.findById(DiaSemana.valueOf(day.name()))
                .orElse(null);
    }
}
