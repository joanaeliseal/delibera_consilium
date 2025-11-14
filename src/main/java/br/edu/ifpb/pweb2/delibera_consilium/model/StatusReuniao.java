package br.edu.ifpb.pweb2.delibera_consilium.model;

public enum StatusReuniao {
    ENCERRADA(1),
    PROGRAMADA(0);

    private final int valor;
    
    private StatusReuniao(int valor){
        this.valor = valor;
    } 

    public int getValor(){
        return valor;
    }

}
