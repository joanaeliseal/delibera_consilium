package br.edu.ifpb.pweb2.delibera_consilium.model;

public class Voto {
    private int id;
    private TipoVoto voto;
    private boolean ausente;
    
    
    public Voto(int id, TipoVoto voto, boolean ausente) {
        this.id = id;
        this.voto = voto;
        this.ausente = ausente;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public TipoVoto getVoto() {
        return voto;
    }


    public void setVoto(TipoVoto voto) {
        this.voto = voto;
    }


    public boolean isAusente() {
        return ausente;
    }


    public void setAusente(boolean ausente) {
        this.ausente = ausente;
    }
    
}

