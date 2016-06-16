package br.edu.unicatolica.meuslivros.entity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gang of Three on 15/06/2016.
 */
public class Emprestimo {
    private Long id;
    private String descricaoLivro;
    private Calendar dataEmprestimo;
    private Calendar dataDevolucao;
    private double multa;
    private String usuarioId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricaoLivro() {
        return descricaoLivro;
    }

    public void setDescricaoLivro(String descricaoLivro) {
        this.descricaoLivro = descricaoLivro;
    }

    public Calendar getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Calendar dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Calendar getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Calendar dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Emprestimo emprestimo = (Emprestimo) o;

        return id.equals(emprestimo.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}