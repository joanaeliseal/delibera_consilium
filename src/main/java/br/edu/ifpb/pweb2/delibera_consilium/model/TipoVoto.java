package br.edu.ifpb.pweb2.delibera_consilium.model;

public enum TipoVoto {
    COM_RELATOR(0),
    DIVERGENTE(1);

    private final int valor;

    private TipoVoto(int valor){
        this.valor = valor;
    }

    public int getValor(){
        return valor;
    }

}
