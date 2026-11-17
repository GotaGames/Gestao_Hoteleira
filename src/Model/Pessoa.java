package Model;

public class Pessoa {

    protected int idPessoa;
    protected String nome;
    protected String contacto;
    protected String endereco;
    protected String sexo;
    protected String documentoIdentificacao;

    public Pessoa() {
    }

    public Pessoa(int idPessoa, String nome, String contacto, String endereco,
                  String sexo, String documentoIdentificacao) {
        this.idPessoa = idPessoa;
        this.nome = nome;
        this.contacto = contacto;
        this.endereco = endereco;
        this.sexo = sexo;
        this.documentoIdentificacao = documentoIdentificacao;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDocumentoIdentificacao() {
        return documentoIdentificacao;
    }

    public void setDocumentoIdentificacao(String documentoIdentificacao) {
        this.documentoIdentificacao = documentoIdentificacao;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "idPessoa=" + idPessoa +
                ", nome='" + nome + '\'' +
                ", contacto='" + contacto + '\'' +
                ", endereco='" + endereco + '\'' +
                ", sexo='" + sexo + '\'' +
                ", documentoIdentificacao='" + documentoIdentificacao + '\'' +
                '}';
    }
}