    package projetojava1;

import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ManipulaBanco {

    Connection cone;

    public ManipulaBanco() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cone = DriverManager.getConnection("jdbc:mysql://127.0.0.1/Hospital", "root", "");
            System.out.println("Conectado");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage() + "Não conectado");
        }
    }

    public boolean Cadastro(String rg, String nome, String senha, String cartao, String cpf, String email, String nascimento, String deficiencia) {//Criar login
        boolean confirmacao;
        try {
            Statement comando = cone.createStatement();
            comando.execute("INSERT INTO `hospital`(`RG`, `Nome`, `Senha`, `Cartao`, `CPF`, `nid`, `Email`, `nascimento`, `deficiencia`) VALUES ('" + rg + "','" + nome + "','" + senha + "','" + cartao + "','" + cpf + "','0','" + email + "','" + nascimento + "','" + deficiencia + "');");
            confirmacao = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            confirmacao = false;
        }

        return confirmacao;
    }

    public boolean Login(String rg, String senha) {
        boolean confirmacao = false;
        try {
            Statement comando = cone.createStatement();
            ResultSet rs = comando.executeQuery("SELECT RG,Senha,Nome FROM `hospital` WHERE RG='" + rg + "';");

            while (rs.next()) {
                String rg1 = rs.getString("RG");
                String senhaa = rs.getString("Senha");
                String nomee = rs.getString("Nome");

                if (rg.equals(rg1) && senha.equals(senhaa)) {//Comparaçao para ver se sao iguais
                    JOptionPane.showMessageDialog(null, "Seja bem vindo: " + nomee);
                    confirmacao = true;
                } else {
                    confirmacao = false;
                }
            }
        } catch (SQLException e) {

             System.out.println(e.getMessage());
            confirmacao = false;
        }

        return confirmacao;
    }

    public String Medicos(String id) {//Pegar o Numero de ID do medico e retornar seu nome
        String nome1=null;
        try {
            Statement comando = cone.createStatement();
            ResultSet rs = comando.executeQuery("SELECT Nome FROM `medicos` WHERE Identificacao='" + id + "';");

            while (rs.next()) {

                nome1 = rs.getString("Nome");
                                
            }
        } catch (SQLException e) {
             System.out.println(e.getMessage());
        }

        return nome1;

    }

    public ArrayList<String> tipoMedico(String tipo) { //Retorna o nome do medico pelo sua funcao
        ArrayList<String> medicos = new ArrayList<>();//Sera utilizada para selecionar o tipo em combobox
        try {
            Statement comando = cone.createStatement();
            ResultSet rs = comando.executeQuery("SELECT Nome,Tipo FROM `medicos` WHERE Tipo='" + tipo + "';");

            while (rs.next()) {

                String tipoEncontrado = rs.getString("Tipo");
                String nome = rs.getString("Nome");

                if (tipo.equals(tipoEncontrado)) {
                    medicos.add(nome);
                }
            }
        } catch (SQLException e) {
             System.out.println(e.getMessage());
        }
        return medicos;
    }

    public ArrayList<String> horarioMedico(String nome) {//Retorna os Horarios de trabalho de cada médico
        ArrayList<String> horario = new ArrayList<>();

        try {
            Statement comando = cone.createStatement();
            ResultSet rs = comando.executeQuery("SELECT Nome,Horario1,Horario2,Horario3,Horario4 FROM `medicos` WHERE Nome='" + nome + "';");

            while (rs.next()) {

                String nomeEncontrado = rs.getString("Nome");
                String horario1 = rs.getString("Horario1");
                String horario2 = rs.getString("Horario2");
                String horario3 = rs.getString("Horario3");
                String horario4 = rs.getString("Horario4");

                if (nome.equals(nomeEncontrado)) {

                    horario.add(horario1);
                    horario.add(horario2);
                    horario.add(horario3);
                    horario.add(horario4);
                }
            }
        } catch (SQLException e) {
             System.out.println(e.getMessage());
        }
        return horario;
    }
    //Cria a consulta no Banco de dados
    public boolean Consulta( String nome, String data,String rg, String cpf, String cartao, String tipo, String doutor, String horario) {
        boolean confirmacao;
        try {
            Statement comando = cone.createStatement();
            comando.execute("INSERT INTO `consultas`(`Nome`, `Data`, `RG`, `CPF`, `Cartao`, `Tipo`, `Doutor`, `Horario`,`DataConsulta`,`Confirmação`) VALUES ('" + nome + "','" + data + "','" + rg + "','" + cpf + "','" + cartao + "','" + tipo + "','" + doutor + "','" + horario + "','NR','Sem Resposta');");
            confirmacao = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            confirmacao = false;
        }

        return confirmacao;
    }
   //Retorna a funcao do medico
    public ArrayList<String> Genero() {
        ArrayList<String> tipos = new ArrayList<>();
        try {
            Statement comando = cone.createStatement();
            ResultSet rs = comando.executeQuery("SELECT DISTINCT Tipo FROM `medicos`;");
             //Distinct serve para nao retornar valores iguais
            while (rs.next()) {

                String tipoEncontrado = rs.getString("Tipo");
               
                    tipos.add(tipoEncontrado);
             
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tipos;
    }
    
    public String[] consultaPaciente(String nome)//Consulta o Nome, Data, Horario da consulta marcada com o paciente
    { 
        String [] res = new String[4];
       try {
           
            Statement comando = cone.createStatement();
          ResultSet resultado =  comando.executeQuery("SELECT Nome,Data,Horario,Confirmação FROM `consultas` WHERE Nome='"+nome+"';");//ExeculteQuery(Execulta comando e trás resultado de uma consulta==Select) Execulte(Não gera resultado)
           if(resultado.next())
           {
               res[0]= resultado.getString("Nome");//Nome da coluna
               res[1] = resultado.getString("Data");
               res[2] = resultado.getString("Horario");
               res[3] = resultado.getString("Confirmação");
           }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }   
       return res;
    }
    
    public ArrayList<String> listarPacientes(String nome)//Pega o nome dos pacientes de acordo com cada medico
    {
       ArrayList<String> pacientes = new ArrayList();
       try {
            Statement comando = cone.createStatement();
          ResultSet resultado =  comando.executeQuery("SELECT Nome FROM `consultas` WHERE Doutor='"+nome+"';");//ExeculteQuery(Execulta comando e trás resultado de uma consulta==Select) Execulte(Não gera resultado)
           
           while(resultado.next())
           {
               pacientes.add(resultado.getString("Nome"));//Nome da coluna
           }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            
        }
       return pacientes;
    }
    
    public void agendaDia(String dia,String nome)
    {
        try {
            Statement comando = cone.createStatement();
            comando.execute("UPDATE `consultas` SET `DataConsulta`='"+dia+"'WHERE Nome='"+nome+"';");

           } catch (SQLException e) {
             System.out.println(e.getMessage());
         }
     }
    
    public String retornaData(String nome) {//Pegar o Numero de ID do medico e retornar seu nome
        String data=null;
        try {
            Statement comando = cone.createStatement();
            ResultSet rs = comando.executeQuery("SELECT DataConsulta FROM `consultas` WHERE Nome='" + nome + "';");

            while (rs.next()) {

                data = rs.getString("DataConsulta");
                                
            }
        } catch (SQLException e) {
             System.out.println(e.getMessage());
        }

        return data;

    }
    
    public String[] checarConsulta(String rg)
    {
        String [] res = new String[3];
       try {
           
            Statement comando = cone.createStatement();
          ResultSet resultado =  comando.executeQuery("SELECT DataConsulta,Doutor,Horario FROM `consultas` WHERE RG='"+rg+"';");//ExeculteQuery(Execulta comando e trás resultado de uma consulta==Select) Execulte(Não gera resultado)
           if(resultado.next())
           {
               res[0]= resultado.getString("DataConsulta");//Nome da coluna
               res[1] = resultado.getString("Doutor");
               res[2] = resultado.getString("Horario");
           }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }   
       return res;
    }
    
    public void confirmação(String rg)
    {
        try {
           Statement comando = cone.createStatement();
           
           comando.execute("UPDATE `consultas` SET `Confirmação`='Confirmado'WHERE RG='"+rg+"'");//ExeculteQuery(Execulta comando e trás resultado de uma consulta==Select) Execulte(Não gera resultado)
           
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }   
    }
    
    public void deletaPaciente(String nome)
    {
       try {
           Statement comando = cone.createStatement();
           
           comando.execute("DELETE FROM `consultas` WHERE `consultas`.`Nome` = '"+nome+"';");
           }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }}
    
    public String dataConsulta(String nome)
    {
        String data = null;
        try {
           Statement comando = cone.createStatement();
           
           ResultSet resultado = comando.executeQuery("SELECT Data FROM `consultas` WHERE Nome='"+nome+"';");
           while (resultado.next()) {

                data = resultado.getString("Data");
                                         
                    }          
           }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
          return data;
    }
}
