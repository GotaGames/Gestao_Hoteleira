package Model;

public class Funcionario extends Pessoa {

    private String cargo;
    private int horarioTrabalho;
    private String nivelPermissao;
    private double salario;

    public Funcionario() {
        super();
    }

    public Funcionario(int idPessoa, String nome, String contacto, String endereco,
                       String sexo, String documentoIdentificacao,
                       String cargo, int horarioTrabalho, String nivelPermissao, double salario) {
        super(idPessoa, nome, contacto, endereco, sexo, documentoIdentificacao);
        this.cargo = cargo;
        this.horarioTrabalho = horarioTrabalho;
        this.nivelPermissao = nivelPermissao;
        this.salario = salario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getHorarioTrabalho() {
        return horarioTrabalho;
    }

    public void setHorarioTrabalho(int horarioTrabalho) {
        this.horarioTrabalho = horarioTrabalho;
    }

    public String getNivelPermissao() {
        return nivelPermissao;
    }

    public void setNivelPermissao(String nivelPermissao) {
        this.nivelPermissao = nivelPermissao;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "idPessoa=" + idPessoa +
                ", nome='" + nome + '\'' +
                ", contacto='" + contacto + '\'' +
                ", endereco='" + endereco + '\'' +
                ", sexo='" + sexo + '\'' +
                ", documentoIdentificacao='" + documentoIdentificacao + '\'' +
                ", cargo='" + cargo + '\'' +
                ", horarioTrabalho=" + horarioTrabalho +
                ", nivelPermissao='" + nivelPermissao + '\'' +
                ", salario=" + salario +
                '}';
    }
}
