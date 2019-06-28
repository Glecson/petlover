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
public class Animal extends javax.swing.JFrame {

    
    Connection con = null;
    PreparedStatement ps = null, ps2 = null;
    ResultSet rs = null, rs2 = null;
    Statement st = null, st2 = null;
    boolean menuDisplay = false;
    
    
    
    public Animal() throws ClassNotFoundException, SQLException {
        initComponents();
        con = Conectabd.conectabd();
        mostrarTabela();
        quantidadeRacaDistintas();
        quantidadeAnimais();
        pegaCliente();
    }
    
    public void pegaCliente(){
        String sql = "SELECT nome FROM cliente";
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                cbNomeDono.addItem(rs.getString("nome"));
            }
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    
    
    public void quantidadeAnimais() throws ClassNotFoundException, SQLException{
        String SQL = "select * from animal";
        
        try{
            ps = con.prepareStatement(SQL);
            rs = ps.executeQuery();
            rs.last();
            int quantidadeAnimais = rs.getRow();
            jlQuantidadeAnimais.setText(Integer.toString(quantidadeAnimais));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    
    
    public void quantidadeRacaDistintas() throws ClassNotFoundException, SQLException{
        String SQL = "select DISTINCT raca from animal";
        
        try{
            ps = con.prepareStatement(SQL);
            rs = ps.executeQuery();
            rs.last();
            int qtdRacaDistintas = rs.getRow();
            jlQtdRacaDistintas.setText(Integer.toString(qtdRacaDistintas));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    public void pesquisarAnimal(){
        String SQL = "SELECT id_animal AS ID, nome AS Nome, tipo AS Tipo, raca AS Raça,"
                + " porte AS Porte, cod_dono AS Dono FROM animal WHERE nome LIKE ? OR tipo LIKE ? OR raca LIKE ?";
        try{
            ps = con.prepareStatement(SQL);
            ps.setString(1, jtxPesquisar.getText()+"%");
            ps.setString(2, jtxPesquisar.getText()+"%");
            ps.setString(3, jtxPesquisar.getText()+"%");
            rs = ps.executeQuery();
            jtbAnimal.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    
    
    public void mostrarTabela() throws ClassNotFoundException{
        String SQL = "select id_animal AS ID,nome AS Nome,tipo AS Tipo,raca AS Raça,"
                + "porte AS Porte,cod_dono from animal";
        con = Conectabd.conectabd();
        try{
            ps = con.prepareStatement(SQL);
            rs = ps.executeQuery();
            jtbAnimal.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    public void setar() throws SQLException{
        int setar = jtbAnimal.getSelectedRow();
        int id_cliente = 0;
        jtfNomeAnimal.setText(jtbAnimal.getModel().getValueAt(setar, 1).toString());
        cbTipoAnimal.setSelectedItem(jtbAnimal.getModel().getValueAt(setar, 2).toString());
        jtfRaca.setText(jtbAnimal.getModel().getValueAt(setar, 3).toString());        
        cbPorte.setSelectedItem(jtbAnimal.getModel().getValueAt(setar, 4).toString());
            
            String nome = "";
            nome = (String) cbNomeDono.getSelectedItem();
            id_cliente = (int) jtbAnimal.getModel().getValueAt(setar, 5);
            String sql = "SELECT * FROM cliente where id_cliente = '"+id_cliente+"'";        
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                nome = rs.getString("nome");
            }        
            cbNomeDono.setSelectedItem(nome);
    }
    
    public void limparCampos(){
        jtfNomeAnimal.setText(null);
        cbNomeDono.setSelectedIndex(0);
        cbTipoAnimal.setSelectedIndex(0);
        jtfRaca.setText(null);
        cbPorte.setSelectedIndex(0);
    }
    
    
    
    public int pegaIDAnimal() throws SQLException{
        String nome = "";
        nome = jtfNomeAnimal.getText();
        
        String sql1 = "SELECT * FROM animal where nome = '"+nome+"'";
        int id_animal = 0;
        
        ps2 = con.prepareStatement(sql1);
        rs2 = ps2.executeQuery();
        while(rs2.next()){
            id_animal = rs2.getInt("id_animal");
        }
        return id_animal;
    }
    
    public int pegaIDCliente() throws SQLException{
        String nome = "";
        nome = (String) cbNomeDono.getSelectedItem();
        
        String sql1 = "SELECT * FROM cliente where nome = '"+nome+"'";
        int id = 0;
        
        ps2 = con.prepareStatement(sql1);
        rs2 = ps2.executeQuery();
        while(rs2.next()){
            id = rs2.getInt("id_cliente");
        }
        return id;
    }
    
    public void editarAnimal() throws SQLException{
        String SQL = "UPDATE animal SET nome = ?, tipo = ?, raca = ?, porte = ? WHERE id_animal = ? AND cod_dono = '"+pegaIDCliente()+"'";
        
        try{         
            ps = con.prepareStatement(SQL);
         
            ps.setString(1, jtfNomeAnimal.getText());
            ps.setString(2, (String) cbTipoAnimal.getSelectedItem());
            ps.setString(3, jtfRaca.getText());
            ps.setString(4, (String) cbPorte.getSelectedItem());
            ps.setInt(5, pegaIDAnimal());
            
//            limpaCampos();
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Atualizacao Concluida");
        }
        catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    
    
    
    public void deletar() throws SQLException{
        String sql = "DELETE from animal WHERE cod_dono ='"+pegaIDCliente()+"' AND id_animal = '"+pegaIDAnimal()+"'";
        String sql2 = "DELETE from atendimento WHERE cod_cliente ='"+pegaIDCliente()+"' AND cod_animal = '"+pegaIDAnimal()+"'";
        
        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            
            ps2 = con.prepareStatement(sql2);
            ps2.executeUpdate();
            ps2.close();
            
            JOptionPane.showMessageDialog(null,"Excluido com Sucesso");
        } catch (SQLException error) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, error);
        }
    }
    
    
    
    public void cadastrar() throws ClassNotFoundException, SQLException{
        con = Conectabd.conectabd();
        
        String SQL = "INSERT INTO animal(nome, tipo, raca, porte, cod_dono)Values(?,?,?,?,?)";
        
        try{
            ps = con.prepareStatement(SQL);
            
            ps.setString(1, jtfNomeAnimal.getText());
            ps.setString(2, (String) cbTipoAnimal.getSelectedItem());
            ps.setString(3, jtfRaca.getText());
            ps.setString(4, (String) cbPorte.getSelectedItem());
            ps.setInt(5, pegaIDCliente());
            ps.execute();
            ps.close();
            
            JOptionPane.showMessageDialog(null, "Animal cadastrado");
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
        jtbAnimal = new javax.swing.JTable();
        jtxPesquisar = new javax.swing.JTextField();
        btnTrazerDados = new javax.swing.JButton();
        cbNomeDono = new javax.swing.JComboBox<>();
        jlTipoAtendimento1 = new javax.swing.JLabel();
        jlNomeAnimal = new javax.swing.JLabel();
        cbTipoAnimal = new javax.swing.JComboBox<>();
        jlTipoAnimal = new javax.swing.JLabel();
        btnCadastrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnLimparCampos = new javax.swing.JButton();
        jlTipoAnimal1 = new javax.swing.JLabel();
        cbPorte = new javax.swing.JComboBox<>();
        jlTipoAnimal2 = new javax.swing.JLabel();
        jtfNomeAnimal = new javax.swing.JTextField();
        jtfRaca = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jlTitulo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlQtdRacaDistintas = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jlQuantidadeAnimais = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnExcluir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Animal");

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

        jtbAnimal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtbAnimal.setGridColor(new java.awt.Color(230, 234, 237));
        jScrollPane2.setViewportView(jtbAnimal);

        jtxPesquisar.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtxPesquisar.setForeground(new java.awt.Color(113, 113, 113));
        jtxPesquisar.setToolTipText("Digite o nome do cliente");
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

        btnTrazerDados.setBackground(new java.awt.Color(42, 52, 57));
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

        cbNomeDono.setBackground(new java.awt.Color(230, 234, 237));
        cbNomeDono.setBorder(null);
        cbNomeDono.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbNomeDono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNomeDonoActionPerformed(evt);
            }
        });

        jlTipoAtendimento1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlTipoAtendimento1.setForeground(new java.awt.Color(113, 113, 113));
        jlTipoAtendimento1.setText("Selecionar dono");

        jlNomeAnimal.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlNomeAnimal.setForeground(new java.awt.Color(113, 113, 113));
        jlNomeAnimal.setText("Nome do Animal");

        cbTipoAnimal.setBackground(new java.awt.Color(230, 234, 237));
        cbTipoAnimal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cachorro", "Gato" }));
        cbTipoAnimal.setBorder(null);
        cbTipoAnimal.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbTipoAnimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoAnimalActionPerformed(evt);
            }
        });

        jlTipoAnimal.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlTipoAnimal.setForeground(new java.awt.Color(113, 113, 113));
        jlTipoAnimal.setText("Tipo");

        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/success.png"))); // NOI18N
        btnCadastrar.setToolTipText("Cadastrar");
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/shuffle.png"))); // NOI18N
        btnEditar.setToolTipText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnLimparCampos.setBackground(new java.awt.Color(255, 255, 255));
        btnLimparCampos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_Cancel_48px.png"))); // NOI18N
        btnLimparCampos.setToolTipText("Limpar campos");
        btnLimparCampos.setBorder(null);
        btnLimparCampos.setBorderPainted(false);
        btnLimparCampos.setFocusPainted(false);
        btnLimparCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparCamposActionPerformed(evt);
            }
        });

        jlTipoAnimal1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlTipoAnimal1.setForeground(new java.awt.Color(113, 113, 113));
        jlTipoAnimal1.setText("Raça");

        cbPorte.setBackground(new java.awt.Color(230, 234, 237));
        cbPorte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mini", "Pequeno", "Médio", "Grande", "Extra grande" }));
        cbPorte.setBorder(null);
        cbPorte.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbPorte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPorteActionPerformed(evt);
            }
        });

        jlTipoAnimal2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlTipoAnimal2.setForeground(new java.awt.Color(113, 113, 113));
        jlTipoAnimal2.setText("Porte");

        jtfNomeAnimal.setBackground(new java.awt.Color(230, 234, 237));
        jtfNomeAnimal.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtfNomeAnimal.setForeground(new java.awt.Color(113, 113, 113));
        jtfNomeAnimal.setToolTipText("Digite o nome do animal");
        jtfNomeAnimal.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jtfRaca.setBackground(new java.awt.Color(230, 234, 237));
        jtfRaca.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtfRaca.setForeground(new java.awt.Color(113, 113, 113));
        jtfRaca.setToolTipText("Digite a raça");
        jtfRaca.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/undraw_modern_woman_9l0u.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );

        jPanel3.setBackground(new java.awt.Color(241, 245, 248));

        jlTitulo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jlTitulo.setForeground(new java.awt.Color(99, 100, 103));
        jlTitulo.setText("Animal");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jlTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jlTitulo)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(248, 248, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(118, 118));

        jlQtdRacaDistintas.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jlQtdRacaDistintas.setForeground(new java.awt.Color(186, 184, 229));
        jlQtdRacaDistintas.setText("0");
        jlQtdRacaDistintas.setToolTipText("Raças distintas");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/diversify.png"))); // NOI18N
        jLabel3.setToolTipText("Raças distintas");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jlQtdRacaDistintas)
                    .addComponent(jLabel3))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jlQtdRacaDistintas)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(248, 248, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(118, 118));

        jlQuantidadeAnimais.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jlQuantidadeAnimais.setForeground(new java.awt.Color(186, 184, 229));
        jlQuantidadeAnimais.setText("0");
        jlQuantidadeAnimais.setToolTipText("Total de animais");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/all.png"))); // NOI18N
        jLabel7.setToolTipText("Total de animais");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jlQuantidadeAnimais)
                    .addComponent(jLabel7))
                .addGap(68, 68, 68))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jlQuantidadeAnimais)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete_sign-24.png"))); // NOI18N
        btnExcluir.setToolTipText("Deletar");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jpSideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 22, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 23, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jlTipoAnimal)
                                                .addGap(0, 288, Short.MAX_VALUE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jlTipoAnimal1)
                                                    .addComponent(jtfRaca)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(cbTipoAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 1, Short.MAX_VALUE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cbNomeDono, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(cbPorte, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jlTipoAtendimento1)
                                                            .addComponent(jlTipoAnimal2))
                                                        .addGap(0, 0, Short.MAX_VALUE)))
                                                .addContainerGap())))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jlNomeAnimal)
                                                .addGap(290, 290, 290))
                                            .addComponent(jtfNomeAnimal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addComponent(jlNomeAnimal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jtfNomeAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jlTipoAnimal)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbTipoAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jlTipoAtendimento1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbNomeDono, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jlTipoAnimal1)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(cbPorte, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jlTipoAnimal2)
                                                    .addGap(43, 43, 43)))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(98, 98, 98)
                                        .addComponent(jtfRaca, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnExcluir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnLimparCampos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jpSideMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTrazerDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrazerDadosActionPerformed
        try {
            setar();
        } catch (SQLException ex) {
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTrazerDadosActionPerformed

    private void jtxPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxPesquisarKeyReleased
        pesquisarAnimal();
    }//GEN-LAST:event_jtxPesquisarKeyReleased

    private void jtxPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxPesquisarActionPerformed
        pesquisarAnimal();
    }//GEN-LAST:event_jtxPesquisarActionPerformed

    private void btnMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuMouseEntered
        if(menuDisplay == true){
            menuDisplay = false;
            btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu-24.png")));
            jpMenu.setSize(0, getHeight());
            jpSideMenu.setSize(50,getHeight());
        }
    }//GEN-LAST:event_btnMenuMouseEntered

    private void btnMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuMouseClicked
        if(menuDisplay == false){
            try {
                mostrarTabela();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
            }
            menuDisplay = true;
            jpMenu.setSize(480, getHeight());
            jpSideMenu.setSize(480,getHeight());
            btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete_sign-24.png")));
        }
    }//GEN-LAST:event_btnMenuMouseClicked

    private void cbPorteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPorteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbPorteActionPerformed

    private void btnLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCamposActionPerformed
       limparCampos();
    }//GEN-LAST:event_btnLimparCamposActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        try {
            editarAnimal();
        } catch (SQLException ex) {
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        try {
            cadastrar();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void cbTipoAnimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoAnimalActionPerformed

    }//GEN-LAST:event_cbTipoAnimalActionPerformed

    private void cbNomeDonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNomeDonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNomeDonoActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        int confirma = JOptionPane.showConfirmDialog(null,"Confirme a exclusão dos dados","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            try {
                deletar();
            } catch (SQLException ex) {
                Logger.getLogger(Atendimento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

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
            java.util.logging.Logger.getLogger(Animal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Animal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Animal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Animal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Animal().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnLimparCampos;
    private javax.swing.JLabel btnMenu;
    private javax.swing.JButton btnTrazerDados;
    private javax.swing.JComboBox<Object> cbNomeDono;
    private javax.swing.JComboBox<String> cbPorte;
    private javax.swing.JComboBox<String> cbTipoAnimal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jlNomeAnimal;
    private javax.swing.JLabel jlQtdRacaDistintas;
    private javax.swing.JLabel jlQuantidadeAnimais;
    private javax.swing.JLabel jlTipoAnimal;
    private javax.swing.JLabel jlTipoAnimal1;
    private javax.swing.JLabel jlTipoAnimal2;
    private javax.swing.JLabel jlTipoAtendimento1;
    private javax.swing.JLabel jlTitulo;
    private javax.swing.JPanel jpMenu;
    private javax.swing.JPanel jpSideMenu;
    private javax.swing.JTable jtbAnimal;
    private javax.swing.JTextField jtfNomeAnimal;
    private javax.swing.JTextField jtfRaca;
    private javax.swing.JTextField jtxPesquisar;
    // End of variables declaration//GEN-END:variables
}
