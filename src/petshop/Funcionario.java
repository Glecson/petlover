/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package petshop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Usuario
 */
public class Funcionario extends javax.swing.JFrame {
    
    
    Connection con = null;
    PreparedStatement ps = null, ps2 = null,ps3 = null;
    ResultSet rs = null, rs2 = null;
    Statement st = null, st2 = null;
    int qtdGatos = 0, qtdCachorros = 0;
    boolean menuDisplay = false;
    
    
    
    String sql = "SELECT nomeCompleto,email,telefone,endereco,cpf,cargo FROM funcionario";
    
    
    

    
    public Funcionario() throws ClassNotFoundException {
        initComponents();
        mostrarTabela();
    }
    
    
    
    public void pesquisarFuncionario(){
        String SQL = "SELECT nomeCompleto AS Nome,email AS Email,telefone AS Telefone"
                + ",endereco AS Logradouro,cpf AS CPF,cargo AS Cargo FROM funcionario "
                + "where nomeCompleto like ? OR cpf like ? OR cargo like ?";
        try{
            ps = con.prepareStatement(SQL);
            ps.setString(1, jtxPesquisar.getText()+"%");
            ps.setString(2, jtxPesquisar.getText()+"%");
            ps.setString(3, jtxPesquisar.getText()+"%");
            rs = ps.executeQuery();
            jtbFuncionario.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    
    
    
    public int pegaIDFuncionario(String cpf) throws SQLException{
        
        String sql1 = "SELECT * FROM funcionario where cpf = '"+cpf+"'";
        int id = 0;
        
        ps2 = con.prepareStatement(sql1);
        rs2 = ps2.executeQuery();
        while(rs2.next()){
            id = rs2.getInt("id_funcionario");
        }
        return id;
    }
    
    public void deletarFuncionario() throws SQLException{
        String sql = "DELETE from funcionario where cpf='"+jtfCPF.getText()+"'";
        String sql2 = "DELETE from users where cod_funcionario = '"+pegaIDFuncionario(jtfCPF.getText())+"'";
        try {
            ps = con.prepareStatement(sql);
            ps2 = con.prepareStatement(sql2);
            jtfEmail.getText();
            ps.executeUpdate();
            ps.close();
            ps2.executeUpdate();
            ps2.close();
            
            limparCampos();
            JOptionPane.showMessageDialog(null,"Excluido com Sucesso");
        } catch (SQLException error) {
            Logger.getLogger(Funcionario.class.getName()).log(Level.SEVERE, null, error);
        }
    }
    
    
    public void editarFuncionario() throws SQLException{
        String SQL = "UPDATE funcionario SET nomeCompleto = ?,telefone = ?,email = ?"
                + ", endereco = ?, cpf = ?, cargo = ? WHERE cpf = ? or nomeCompleto = ?";
        
        try{         
            ps = con.prepareStatement(SQL);
         
            ps.setString(1, jtfNomeCompleto.getText());
            ps.setString(2, jfTelefone.getText());
            ps.setString(3, jtfEmail.getText());
            ps.setString(4, jtfLogradouro.getText());
            ps.setString(5, jtfCPF.getText());
            ps.setString(6, jtfCargo.getText());
            ps.setString(7, jtfCPF.getText());
            ps.setString(8, jtfNomeCompleto.getText());
       
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Atualizacao Concluida");
        }
        catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }

    
    public void setar(){
        int setar = jtbFuncionario.getSelectedRow();
        
        jtfNomeCompleto.setText(jtbFuncionario.getModel().getValueAt(setar, 0).toString());
        jtfEmail.setText(jtbFuncionario.getModel().getValueAt(setar, 1).toString());
        jfTelefone.setText(jtbFuncionario.getModel().getValueAt(setar, 2).toString());        
        jtfLogradouro.setText(jtbFuncionario.getModel().getValueAt(setar, 3).toString());
        jtfCPF.setText(jtbFuncionario.getModel().getValueAt(setar, 4).toString());
        jtfCargo.setText(jtbFuncionario.getModel().getValueAt(setar, 5).toString());
    }
    
    public void limparCampos(){
        jtfNomeCompleto.setText("");
        jtfEmail.setText("");
        jtfCPF.setText("");
        jfTelefone.setText("");
        jtfLogradouro.setText("");
        jtfCargo.setText("");
    }
    
    
    
    public void cadastrar() throws ClassNotFoundException{
        con = Conectabd.conectabd();
        
        String SQL = "INSERT INTO funcionario(nomeCompleto,email, cpf, telefone, endereco, cargo)Values(?,?,?,?,?,?)";
        
        try{
            ps = con.prepareStatement(SQL);
            
            ps.setString(1, jtfNomeCompleto.getText());
            ps.setString(2, jtfEmail.getText());
            ps.setString(3, jtfCPF.getText());
            ps.setString(4, jfTelefone.getText());
            ps.setString(5, jtfLogradouro.getText());
            ps.setString(6, jtfCargo.getText());
            ps.execute();
            ps.close();
            
            jtfNomeCompleto.setText("");
            jtfEmail.setText("");
            jtfCPF.setText("");
            jfTelefone.setText("");
            jtfLogradouro.setText("");
            jtfCargo.setText("");
            
            JOptionPane.showMessageDialog(null, "Funcionario cadastrado");
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    public void mostrarTabela() throws ClassNotFoundException{
        String SQL = "SELECT nomeCompleto AS Nome,email AS Email,telefone AS Telefone "
                + ",endereco AS Endereço,cpf AS CPF,cargo AS Cargo FROM funcionario";
        con = Conectabd.conectabd();
        try{
            ps = con.prepareStatement(SQL);
            rs = ps.executeQuery();
            jtbFuncionario.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jpSideMenu = new javax.swing.JPanel();
        btnMenu = new javax.swing.JLabel();
        jpMenu = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbFuncionario = new javax.swing.JTable();
        jtxPesquisar = new javax.swing.JTextField();
        btnTrazerDados = new javax.swing.JButton();
        jtfNomeCompleto = new javax.swing.JTextField();
        jlNomeAnimal = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jpHeader3 = new javax.swing.JPanel();
        jlTitulo3 = new javax.swing.JLabel();
        jtfCPF = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        btnCadastrar = new javax.swing.JButton();
        jtfLogradouro = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jfTelefone = new javax.swing.JFormattedTextField();
        btnEditar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        iconMulherPhone = new javax.swing.JLabel();
        btnDeletarFuncionario = new javax.swing.JButton();
        jtfCargo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btnLimparCampos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jpSideMenu.setBackground(new java.awt.Color(250, 250, 250));

        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu-24.png"))); // NOI18N
        btnMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMenuMouseEntered(evt);
            }
        });

        jpMenu.setBackground(new java.awt.Color(237, 242, 248));

        jtbFuncionario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nome", "Telefone", "CPF", "Endereço"
            }
        ));
        jScrollPane2.setViewportView(jtbFuncionario);

        jtxPesquisar.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtxPesquisar.setForeground(new java.awt.Color(113, 113, 113));
        jtxPesquisar.setToolTipText("Digite o nome / cpf / cargo do funcionario");
        jtxPesquisar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jtxPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxPesquisarActionPerformed(evt);
            }
        });
        jtxPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxPesquisarKeyReleased(evt);
            }
        });

        btnTrazerDados.setBackground(new java.awt.Color(237, 242, 248));
        btnTrazerDados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_Arrow_48px.png"))); // NOI18N
        btnTrazerDados.setBorder(null);
        btnTrazerDados.setBorderPainted(false);
        btnTrazerDados.setFocusPainted(false);
        btnTrazerDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrazerDadosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpMenuLayout = new javax.swing.GroupLayout(jpMenu);
        jpMenu.setLayout(jpMenuLayout);
        jpMenuLayout.setHorizontalGroup(
            jpMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                    .addComponent(jtxPesquisar)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpMenuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnTrazerDados)))
                .addContainerGap())
        );
        jpMenuLayout.setVerticalGroup(
            jpMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtxPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTrazerDados)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpSideMenuLayout = new javax.swing.GroupLayout(jpSideMenu);
        jpSideMenu.setLayout(jpSideMenuLayout);
        jpSideMenuLayout.setHorizontalGroup(
            jpSideMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSideMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpSideMenuLayout.setVerticalGroup(
            jpSideMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSideMenuLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnMenu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jpMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jtfNomeCompleto.setBackground(new java.awt.Color(230, 234, 237));
        jtfNomeCompleto.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtfNomeCompleto.setForeground(new java.awt.Color(113, 113, 113));
        jtfNomeCompleto.setToolTipText("Digite o nome do cliente");
        jtfNomeCompleto.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jlNomeAnimal.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlNomeAnimal.setForeground(new java.awt.Color(113, 113, 113));
        jlNomeAnimal.setText("Nome completo");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(113, 113, 113));
        jLabel6.setText("Whatsapp");

        jtfEmail.setBackground(new java.awt.Color(230, 234, 237));
        jtfEmail.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtfEmail.setForeground(new java.awt.Color(113, 113, 113));
        jtfEmail.setToolTipText("Digite o CPF do Dono");
        jtfEmail.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(113, 113, 113));
        jLabel7.setText("Email");

        jpHeader3.setBackground(new java.awt.Color(241, 245, 248));

        jlTitulo3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jlTitulo3.setForeground(new java.awt.Color(99, 100, 103));
        jlTitulo3.setText("Funcionario");

        javax.swing.GroupLayout jpHeader3Layout = new javax.swing.GroupLayout(jpHeader3);
        jpHeader3.setLayout(jpHeader3Layout);
        jpHeader3Layout.setHorizontalGroup(
            jpHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHeader3Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jlTitulo3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpHeader3Layout.setVerticalGroup(
            jpHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpHeader3Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addComponent(jlTitulo3)
                .addGap(41, 41, 41))
        );

        jtfCPF.setBackground(new java.awt.Color(230, 234, 237));
        jtfCPF.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtfCPF.setForeground(new java.awt.Color(113, 113, 113));
        jtfCPF.setToolTipText("");
        jtfCPF.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(113, 113, 113));
        jLabel10.setText("CPF");

        btnCadastrar.setBackground(new java.awt.Color(34, 209, 144));
        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/success.png"))); // NOI18N
        btnCadastrar.setToolTipText("Cadastrar");
        btnCadastrar.setPreferredSize(new java.awt.Dimension(41, 41));
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        jtfLogradouro.setBackground(new java.awt.Color(230, 234, 237));
        jtfLogradouro.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtfLogradouro.setForeground(new java.awt.Color(113, 113, 113));
        jtfLogradouro.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(113, 113, 113));
        jLabel11.setText("Logradouro");

        jfTelefone.setBackground(new java.awt.Color(230, 234, 237));
        jfTelefone.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jfTelefone.setForeground(new java.awt.Color(113, 113, 113));
        try {
            jfTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) #####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        btnEditar.setBackground(new java.awt.Color(255, 255, 255));
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/shuffle.png"))); // NOI18N
        btnEditar.setToolTipText("Editar");
        btnEditar.setPreferredSize(new java.awt.Dimension(41, 41));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 382, Short.MAX_VALUE)
        );

        iconMulherPhone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/undraw_working_remotely_jh40.png"))); // NOI18N

        btnDeletarFuncionario.setBackground(new java.awt.Color(255, 255, 255));
        btnDeletarFuncionario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete_sign-24.png"))); // NOI18N
        btnDeletarFuncionario.setToolTipText("Deletar");
        btnDeletarFuncionario.setPreferredSize(new java.awt.Dimension(41, 41));
        btnDeletarFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarFuncionarioActionPerformed(evt);
            }
        });

        jtfCargo.setBackground(new java.awt.Color(230, 234, 237));
        jtfCargo.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtfCargo.setForeground(new java.awt.Color(113, 113, 113));
        jtfCargo.setToolTipText("");
        jtfCargo.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(113, 113, 113));
        jLabel12.setText("Cargo");

        btnLimparCampos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_Cancel_48px.png"))); // NOI18N
        btnLimparCampos.setToolTipText("Limpar campos");
        btnLimparCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparCamposActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpHeader3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jpSideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(iconMulherPhone)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(76, 76, 76)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeletarFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jtfCPF, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jtfNomeCompleto, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jlNomeAnimal, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jfTelefone, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel10))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7)
                                .addComponent(jtfEmail)
                                .addComponent(jtfLogradouro)
                                .addComponent(jLabel12)
                                .addComponent(jtfCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11))))
                .addGap(31, 31, 31))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jpHeader3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jpSideMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                        .addComponent(iconMulherPhone)
                        .addContainerGap(56, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(70, 70, 70))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jlNomeAnimal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfNomeCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jfTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtfLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(6, 6, 6)
                                .addComponent(jtfCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(6, 6, 6)
                                .addComponent(jtfCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeletarFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
            try {
                cadastrar();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Funcionario.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        try {
            editarFuncionario();
        } catch (SQLException ex) {
            Logger.getLogger(Funcionario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuMouseClicked
        if(menuDisplay == false){
            try {
                mostrarTabela();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Funcionario.class.getName()).log(Level.SEVERE, null, ex);
            }
            menuDisplay = true;
            jpMenu.setSize(480, getHeight());
            jpSideMenu.setSize(480,getHeight());
            btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete_sign-24.png")));
        }

    }//GEN-LAST:event_btnMenuMouseClicked

    private void btnMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuMouseEntered
        if(menuDisplay == true){
            menuDisplay = false;
            btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu-24.png")));
            jpMenu.setSize(0, getHeight());
            jpSideMenu.setSize(50,getHeight());
        }
    }//GEN-LAST:event_btnMenuMouseEntered

    private void jtxPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxPesquisarActionPerformed
        pesquisarFuncionario();
    }//GEN-LAST:event_jtxPesquisarActionPerformed

    private void jtxPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxPesquisarKeyReleased
        pesquisarFuncionario();
    }//GEN-LAST:event_jtxPesquisarKeyReleased

    private void btnTrazerDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrazerDadosActionPerformed
        setar();
    }//GEN-LAST:event_btnTrazerDadosActionPerformed

    private void btnDeletarFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarFuncionarioActionPerformed
        try {
            deletarFuncionario();
        } catch (SQLException ex) {
            Logger.getLogger(Funcionario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDeletarFuncionarioActionPerformed

    private void btnLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCamposActionPerformed
        limparCampos();
    }//GEN-LAST:event_btnLimparCamposActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Funcionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Funcionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Funcionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Funcionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Funcionario().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Funcionario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnDeletarFuncionario;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnLimparCampos;
    private javax.swing.JLabel btnMenu;
    private javax.swing.JButton btnTrazerDados;
    private javax.swing.JLabel iconMulherPhone;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JFormattedTextField jfTelefone;
    private javax.swing.JLabel jlNomeAnimal;
    private javax.swing.JLabel jlTitulo3;
    private javax.swing.JPanel jpHeader3;
    private javax.swing.JPanel jpMenu;
    private javax.swing.JPanel jpSideMenu;
    private javax.swing.JTable jtbFuncionario;
    private javax.swing.JTextField jtfCPF;
    private javax.swing.JTextField jtfCargo;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfLogradouro;
    private javax.swing.JTextField jtfNomeCompleto;
    private javax.swing.JTextField jtxPesquisar;
    // End of variables declaration//GEN-END:variables
}
