package br.com.xavecoding.regescweb.controllers;

import br.com.xavecoding.regescweb.dto.RequisicaoFormProfessor;
import br.com.xavecoding.regescweb.models.Professor;
import br.com.xavecoding.regescweb.models.StatusProfessor;
import br.com.xavecoding.regescweb.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/professores")
public class ProfessorController {
    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping("")
    public ModelAndView index() {
        List<Professor> professores = this.professorRepository.findAll();
        ModelAndView mv = new ModelAndView("professores/index");
        mv.addObject("professores", professores);

        return mv;
    }

    @GetMapping("/new")
    public ModelAndView nnew(RequisicaoFormProfessor requisicao) {
//        para uma String --> return "professores/new";

        ModelAndView mv = new ModelAndView("professores/new");
        mv.addObject("listaStatusProfessor", StatusProfessor.values());

        return mv;
    }

//    Web Parameter Tampering -> DTO - Data Transfer Object (Objeto de Transferência de Dados)
    @PostMapping("")
    public ModelAndView create(@Valid RequisicaoFormProfessor requisicao, BindingResult bindingResult) {
        System.out.println(requisicao);
        if(bindingResult.hasErrors()) {
            System.out.println("\n*********** TEM ERROS ***********\n");

            ModelAndView mv = new ModelAndView("professores/new");
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        }
        else {
            Professor professor = requisicao.toProfessor();
            this.professorRepository.save(professor);

            return new ModelAndView("redirect:/professores/" + professor.getId());
        }
    }

    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable Long id) {
        Optional<Professor> optional = this.professorRepository.findById(id);

        if(optional.isPresent()) {
            Professor professor = optional.get();

            ModelAndView mv = new ModelAndView("/professores/show");
            mv.addObject("professor", professor);

            return mv;
        }
//        Não achou um registro na tabela professor ou o id informado
        else {
            System.out.println("$$$$$$$$$$$ NÃO ACHOU O PROFESSOR DE ID: " + id + " $$$$$$$$$$$");
            return new ModelAndView("redirect:/professores");
        }
    }

    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormProfessor requisicao) {
        Optional<Professor> optional = this.professorRepository.findById(id);

        if(optional.isPresent()) {
            Professor professor = optional.get();
            requisicao.fromProfessor(professor);

            ModelAndView mv = new ModelAndView("professores/edit");
            mv.addObject("professorId", professor.getId());
            mv.addObject("listaStatusProfessor", StatusProfessor.values());

            return mv;
        }
//        Não achou um registro na tabela professor ou o id informado
        else {
            System.out.println("$$$$$$$$$$$ NÃO ACHOU O PROFESSOR DE ID: " + id + " $$$$$$$$$$$");
            return new ModelAndView("redirect:/professores");
        }
    }
}
