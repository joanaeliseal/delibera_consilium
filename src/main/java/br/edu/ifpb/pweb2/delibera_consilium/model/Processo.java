package br.edu.ifpb.pweb2.delibera_consilium.model;

import java.util.Date;

public class Processo {
    private int id;
    private String numero;
    private Date dataRecepcao;
    private Date dataDistribuicao;
    private Date dataParecer;
    private byte parecer;
    private tipoDecisao decisaoRelator;
    
    
    public Processo(int id, String numero, Date dataRecepcao, Date dataDistribuicao, Date dataParecer, byte parecer,
            tipoDecisao decisaoRelator) {
        this.id = id;
        this.numero = numero;
        this.dataRecepcao = dataRecepcao;
        this.dataDistribuicao = dataDistribuicao;
        this.dataParecer = dataParecer;
        this.parecer = parecer;
        this.decisaoRelator = decisaoRelator;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getNumero() {
        return numero;
    }


    public void setNumero(String numero) {
        this.numero = numero;
    }


    public Date getDataRecepcao() {
        return dataRecepcao;
    }


    public void setDataRecepcao(Date dataRecepcao) {
        this.dataRecepcao = dataRecepcao;
    }


    public Date getDataDistribuicao() {
        return dataDistribuicao;
    }


    public void setDataDistribuicao(Date dataDistribuicao) {
        this.dataDistribuicao = dataDistribuicao;
    }


    public Date getDataParecer() {
        return dataParecer;
    }


    public void setDataParecer(Date dataParecer) {
        this.dataParecer = dataParecer;
    }


    public byte getParecer() {
        return parecer;
    }


    public void setParecer(byte parecer) {
        this.parecer = parecer;
    }


    public tipoDecisao getDecisaoRelator() {
        return decisaoRelator;
    }


    public void setDecisaoRelator(tipoDecisao decisaoRelator) {
        this.decisaoRelator = decisaoRelator;
    }

}
