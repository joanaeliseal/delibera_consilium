package br.edu.ifpb.pweb2.delibera_consilium.model;

public enum TipoDecisao {

    DEFERIMENTO(1),
    INDEFERIMENTO(0); // Geralmente 1 para 'sim/positivo' e 0 para 'nao/negativo'

    // 2. Campo privado e final para armazenar o valor int
    private final int valor;

    private TipoDecisao(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

}
