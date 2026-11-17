package Model;

public class Cliente extends Pessoa {

    private String nacionalidade;

    public Cliente() {
        super();
    }

    public Cliente(int idPessoa, String nome, String contacto, String endereco,
                   String sexo, String documentoIdentificacao, String nacionalidade) {
        super(idPessoa, nome, contacto, endereco, sexo, documentoIdentificacao);
        this.nacionalidade = nacionalidade;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idPessoa=" + idPessoa +
                ", nome='" + nome + '\'' +
                ", contacto='" + contacto + '\'' +
                ", endereco='" + endereco + '\'' +
                ", sexo='" + sexo + '\'' +
                ", documentoIdentificacao='" + documentoIdentificacao + '\'' +
                ", nacionalidade='" + nacionalidade + '\'' +
                '}';
    }
}
