/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package petshop;
import java.sql.*;
import javax.swing.JOptionPane;
import java.sql.DriverManager;

public class Conectabd {
    public static Connection conectabd() throws ClassNotFoundException{
        try{
            Class.forName("org.gjt.mm.mysql.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/petshop","root","");
            System.out.println("Banco de Dados Conectado");
            
            return con;            
        }catch(SQLException erro){
            JOptionPane.showMessageDialog(null,erro);
             return null;
        }
    }
}
