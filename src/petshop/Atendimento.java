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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Usuario
 */
public class Atendimento extends javax.swing.JFrame {

    /**
     * Creates new form Atendimento
     */
    Connection con = null;
    PreparedStatement ps = null, ps2 = null;
    ResultSet rs = null, rs2 = null;
    boolean menuDisplay = false;
    
    
    
    public Atendimento() throws ClassNotFoundException, SQLException {
        initComponents();
        con = Conectabd.conectabd();
        this.setLocationRelativeTo(null);
        pegaCliente();
        pegaIDAnimal();
        pegaAnimal();
        jtfIDAtendimento.setVisible(false);
        jtfValorRecebido.setVisible(false);
        jlValorRecebido.setVisible(false);
        jtfTroco.setVisible(false);
        jlTroco.setVisible(false);
        jtfTroco.setVisible(false);
        
        
//        mostrarTabela();
    }
    
    
    
    public void alterarAtendimento() throws SQLException{
        String SQL = "UPDATE atendimento SET status = ?, valorAtendimento = ?, "
                + "tipoAtendimento = ?, cod_cliente = ?, horarioInicio = ?,dataAtendimento = ?, cod_animal = ? where id_atendimento = "+jtfIDAtendimento.getText()+"";
        
        try{         
            ps = con.prepareStatement(SQL);
            
            
            ps.setString(1, (String) cbStatus.getSelectedItem());
            ps.setFloat(2, Float.parseFloat(tfValor.getText()));
            ps.setString(3, (String) cbTipoAtendimento.getSelectedItem());
            ps.setInt(4, pegaIDCliente());
            ps.setString(5, (String) cbHorario.getSelectedItem());
            ps.setDate(6, new java.sql.Date(jdcDiaAtendimento.getDate().getTime()));
            ps.setInt(7, pegaIDAnimal());
       
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Atualizacao Concluida");
        }
        catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    
    public void mostrarTabela() throws ClassNotFoundException{
        String SQL = "SELECT DISTINCT id_atendimento AS ID, status AS Status,tipoAtendimento AS 'Tipo de Atendimento',dataAtendimento AS Data,horarioInicio AS Horario,"
                + "valorAtendimento AS Valor,cod_cliente,c.nome AS Cliente,an.nome AS Animal,cod_animal from atendimento a "
                + "INNER JOIN cliente c on cod_cliente = id_cliente "
                + "INNER JOIN animal an on cod_animal = id_animal where cod_cliente = id_cliente";
        con = Conectabd.conectabd();
        try{
            ps = con.prepareStatement(SQL);
            rs = ps.executeQuery();
            jtbAtendimento.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    
    public void limpaCampos(){
        cbStatus.setSelectedIndex(0);
        cbTipoAtendimento.setSelectedIndex(0);
        cbHorario.setSelectedIndex(0);
        tfValor.setText(null);
        cbNomeCliente.setSelectedIndex(0);
        cbNomeAnimal.setSelectedIndex(0);
    }
    
    public void pegaTroco(){
        
        if(tfValor.getText() == null || jtfValorRecebido == null){
            jtfTroco.setText("");
        }else if(tfValor.getText() != null || jtfValorRecebido != null){
            float troco, total = 0, recebido = 0; 
            total = Float.parseFloat(tfValor.getText());
            recebido = Float.parseFloat(jtfValorRecebido.getText());
            troco = recebido - total;
            jtfTroco.setText(Float.toString(troco));
        }
        
        
    }
    
    
    public void pesquisarAtendimento(){
        String SQL = "SELECT DISTINCT id_atendimento AS ID, status AS Status,tipoAtendimento AS 'Tipo de Atendimento',dataAtendimento AS Data,horarioInicio AS Horario,"
                + "valorAtendimento AS Valor,cod_cliente,c.nome AS Cliente,an.nome AS Animal,cod_animal from atendimento a "
                + "INNER JOIN cliente c on cod_cliente = id_cliente "
                + "INNER JOIN animal an on cod_animal = id_animal"
                + " where cod_cliente = id_cliente AND an.nome like ? OR c.nome like ? OR status LIKE ? OR tipoAtendimento LIKE ?";
        try{
            ps = con.prepareStatement(SQL);
            ps.setString(1, jtxPesquisar.getText()+"%");
            ps.setString(2, jtxPesquisar.getText()+"%");
            ps.setString(3, jtxPesquisar.getText()+"%");
            ps.setString(4, jtxPesquisar.getText()+"%");
            rs = ps.executeQuery();
            jtbAtendimento.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    
   
    
    
    
    public void setar() throws SQLException{
        int setar = jtbAtendimento.getSelectedRow();
        int id_cliente = 0, cod_animal;
        jtfIDAtendimento.setText(jtbAtendimento.getModel().getValueAt(setar, 0).toString());
        cbStatus.setSelectedItem(jtbAtendimento.getModel().getValueAt(setar, 1).toString());
        cbTipoAtendimento.setSelectedItem(jtbAtendimento.getModel().getValueAt(setar, 2).toString());
        jdcDiaAtendimento.setDate((Date) jtbAtendimento.getModel().getValueAt(setar, 3));
        cbHorario.setSelectedItem(jtbAtendimento.getModel().getValueAt(setar, 4).toString());
        tfValor.setText(jtbAtendimento.getModel().getValueAt(setar, 5).toString());
        
            String nome = "";
            nome = (String) cbNomeCliente.getSelectedItem();
            id_cliente = (int) jtbAtendimento.getModel().getValueAt(setar, 6);
            String sql = "SELECT * FROM cliente where id_cliente = '"+id_cliente+"'";        
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                nome = rs.getString("nome");
            }        
            cbNomeCliente.setSelectedItem(nome);
            
            String nomeAnimal = "";
            //nomeAnimal = (String) cbNomeAnimal.setSelectedItem(con);
            cod_animal = (int) jtbAtendimento.getModel().getValueAt(setar, 9);
            String sql2 = "SELECT * FROM animal where id_animal = '"+cod_animal+"'";        
            ps2 = con.prepareStatement(sql2);
            rs2 = ps2.executeQuery();
            while(rs2.next()){
                cbNomeAnimal.removeAllItems();
                nomeAnimal = rs2.getString("nome");
            }
            
            cbNomeAnimal.addItem(nomeAnimal);
    }
    
    public void pegaCliente(){
        
        String sql = "SELECT * FROM cliente";
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                cbNomeCliente.addItem(rs.getString("nome"));
            }
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void pegaAnimal() throws SQLException{
        cbNomeAnimal.removeAllItems();    
        
        String sql = "SELECT * FROM animal where cod_dono = '"+pegaIDCliente()+"'";
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                cbNomeAnimal.addItem(rs.getString("nome"));
            }
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
        
    
    public int pegaIDCliente() throws SQLException{
        String nome = "";
        nome = (String) cbNomeCliente.getSelectedItem();
        
        String sql1 = "SELECT * FROM cliente where nome = '"+nome+"'";
        int id = 0;
        
        ps2 = con.prepareStatement(sql1);
        rs2 = ps2.executeQuery();
        while(rs2.next()){
            id = rs2.getInt("id_cliente");
        }
        return id;
    }
    
    
    
    public int pegaIDAnimal() throws SQLException{
        String nome = "";
        nome = (String) cbNomeAnimal.getSelectedItem();
        
        String sql1 = "SELECT * FROM animal where nome = '"+nome+"'";
        int id_animal = 0;
        
        ps2 = con.prepareStatement(sql1);
        rs2 = ps2.executeQuery();
        while(rs2.next()){
            id_animal = rs2.getInt("id_animal");
        }
        return id_animal;
    }
    
    
    public void cadastrar() throws ClassNotFoundException{
        con = Conectabd.conectabd();
        
        String SQL = "INSERT INTO atendimento(status,tipoAtendimento,dataAtendimento,"
                + "horarioInicio,valorAtendimento,cod_cliente,cod_animal)"
                + "Values(?,?,?,?,?,?,?)";
        
        try{
            ps = con.prepareStatement(SQL);
            
            ps.setString(1, (String) cbStatus.getSelectedItem());
            ps.setString(2, (String) cbTipoAtendimento.getSelectedItem());
            ps.setDate(3, new java.sql.Date(jdcDiaAtendimento.getDate().getTime()));
            ps.setString(4, (String) cbHorario.getSelectedItem());
            ps.setFloat(5, Float.parseFloat(tfValor.getText()));
            ps.setInt(6, pegaIDCliente());
            ps.setInt(7, pegaIDAnimal());
            
            if(tfValor.getText() != null){
                tfValor.setText("");
                ps.execute();
                ps.close();
                JOptionPane.showMessageDialog(null, "Atendimento cadastrado");
            }else{
                JOptionPane.showMessageDialog(null, "É preciso selecionar o tipo de atendimento!");
                cbTipoAtendimento.setFocusable(rootPaneCheckingEnabled);
            }
            
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    
    public void deletar() throws SQLException{
        String sql = "DELETE from atendimento WHERE cod_cliente ='"+pegaIDCliente()+"' AND id_atendimento = '"+jtfIDAtendimento.getText()+"'";
        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            
            JOptionPane.showMessageDialog(null,"Excluido com Sucesso");
        } catch (SQLException error) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, error);
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

        jpBackground = new javax.swing.JPanel();
        jpSideMenu = new javax.swing.JPanel();
        btnMenu = new javax.swing.JLabel();
        jpMenu = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbAtendimento = new javax.swing.JTable();
        jtxPesquisar = new javax.swing.JTextField();
        btnTrazerDados = new javax.swing.JButton();
        jpHeader = new javax.swing.JPanel();
        jlTitulo = new javax.swing.JLabel();
        tfValor = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        jlTipoAtendimento7 = new javax.swing.JLabel();
        jtfIDAtendimento = new javax.swing.JTextField();
        jlTipoAtendimento = new javax.swing.JLabel();
        btnCadastrar = new javax.swing.JButton();
        cbTipoAtendimento = new javax.swing.JComboBox<>();
        btnLimparCampos = new javax.swing.JButton();
        jlNomeCliente = new javax.swing.JLabel();
        cbNomeCliente = new javax.swing.JComboBox<>();
        cbHorario = new javax.swing.JComboBox<>();
        jlHorario = new javax.swing.JLabel();
        cbMetodoPag = new javax.swing.JComboBox<>();
        jlTipoAtendimento6 = new javax.swing.JLabel();
        jtfValorRecebido = new javax.swing.JTextField();
        jlValorRecebido = new javax.swing.JLabel();
        jtfTroco = new javax.swing.JTextField();
        jlTroco = new javax.swing.JLabel();
        jdcDiaAtendimento = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        iconMulherPhone = new javax.swing.JLabel();
        cbNomeAnimal = new javax.swing.JComboBox<>();
        jlNomeAnimal = new javax.swing.JLabel();
        btnAlterarDados = new javax.swing.JButton();
        btnLimparCampos1 = new javax.swing.JButton();
        btnAnimalADD = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Atendimento");
        setBackground(new java.awt.Color(255, 255, 255));

        jpBackground.setBackground(new java.awt.Color(255, 255, 255));

        jpSideMenu.setBackground(new java.awt.Color(250, 250, 250));

        btnMenu.setBackground(new java.awt.Color(248, 248, 255));
        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu-24.png"))); // NOI18N
        btnMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMenuMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMenuMouseEntered(evt);
            }
        });

        jpMenu.setBackground(new java.awt.Color(248, 248, 255));

        jtbAtendimento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtbAtendimento.setGridColor(new java.awt.Color(230, 234, 237));
        jScrollPane2.setViewportView(jtbAtendimento);

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
                .addContainerGap(101, Short.MAX_VALUE))
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

        jpHeader.setBackground(new java.awt.Color(241, 245, 248));

        jlTitulo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jlTitulo.setForeground(new java.awt.Color(99, 100, 103));
        jlTitulo.setText("Atendimento");

        tfValor.setEditable(false);
        tfValor.setBackground(new java.awt.Color(230, 234, 237));
        tfValor.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tfValor.setForeground(new java.awt.Color(113, 113, 113));
        tfValor.setToolTipText("Valor total");
        tfValor.setAutoscrolls(false);
        tfValor.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tfValor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfValorActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(230, 234, 237));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(113, 113, 113));
        jLabel1.setText("R$");

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Agendado", "Recebido", "Executando", "Executado", "Entregue" }));
        cbStatus.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jlTipoAtendimento7.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlTipoAtendimento7.setForeground(new java.awt.Color(113, 113, 113));
        jlTipoAtendimento7.setText("Status");

        jtfIDAtendimento.setBackground(new java.awt.Color(230, 234, 237));
        jtfIDAtendimento.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jtfIDAtendimento.setEnabled(false);
        jtfIDAtendimento.setFocusable(false);

        javax.swing.GroupLayout jpHeaderLayout = new javax.swing.GroupLayout(jpHeader);
        jpHeader.setLayout(jpHeaderLayout);
        jpHeaderLayout.setHorizontalGroup(
            jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHeaderLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jlTitulo)
                .addGap(568, 568, 568)
                .addComponent(jtfIDAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpHeaderLayout.createSequentialGroup()
                        .addComponent(jlTipoAtendimento7)
                        .addGap(185, 185, 185)))
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfValor, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpHeaderLayout.setVerticalGroup(
            jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHeaderLayout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpHeaderLayout.createSequentialGroup()
                        .addComponent(jlTitulo)
                        .addGap(41, 41, 41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpHeaderLayout.createSequentialGroup()
                        .addGroup(jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpHeaderLayout.createSequentialGroup()
                                .addComponent(jlTipoAtendimento7)
                                .addGap(3, 3, 3)
                                .addGroup(jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtfIDAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jpHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tfValor, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)))
                        .addGap(24, 24, 24))))
        );

        jlTipoAtendimento.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlTipoAtendimento.setForeground(new java.awt.Color(113, 113, 113));
        jlTipoAtendimento.setText("Tipo de Atendimento");

        btnCadastrar.setBackground(new java.awt.Color(34, 209, 144));
        btnCadastrar.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/success.png"))); // NOI18N
        btnCadastrar.setToolTipText("Cadastrar");
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        cbTipoAtendimento.setBackground(new java.awt.Color(230, 234, 237));
        cbTipoAtendimento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Banho", "Tosa", "Banho+Tosa" }));
        cbTipoAtendimento.setBorder(null);
        cbTipoAtendimento.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbTipoAtendimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoAtendimentoActionPerformed(evt);
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

        jlNomeCliente.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlNomeCliente.setForeground(new java.awt.Color(113, 113, 113));
        jlNomeCliente.setText("Selecionar Cliente");
        jlNomeCliente.setToolTipText("");

        cbNomeCliente.setBackground(new java.awt.Color(230, 234, 237));
        cbNomeCliente.setToolTipText("Selecione o dono");
        cbNomeCliente.setBorder(null);
        cbNomeCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbNomeCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbNomeClienteFocusLost(evt);
            }
        });
        cbNomeCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNomeClienteActionPerformed(evt);
            }
        });

        cbHorario.setBackground(new java.awt.Color(230, 234, 237));
        cbHorario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "08:00 - 08:30", "09:00 - 09:30", "10:00 - 10:30", "11:00 - 11:30", "12:00 - 12:30", "13:00 - 13:30", "14:00 - 14:30", "15:00 - 15:30", "16:00 - 16:30", "17:00 - 17:30", "18:00 - 18:30" }));

        jlHorario.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlHorario.setForeground(new java.awt.Color(113, 113, 113));
        jlHorario.setText("Horário Atendimento");

        cbMetodoPag.setBackground(new java.awt.Color(230, 234, 237));
        cbMetodoPag.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cartão", "Dinheiro" }));
        cbMetodoPag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMetodoPagActionPerformed(evt);
            }
        });

        jlTipoAtendimento6.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlTipoAtendimento6.setForeground(new java.awt.Color(113, 113, 113));
        jlTipoAtendimento6.setText("Método de pagamento");

        jtfValorRecebido.setBackground(new java.awt.Color(230, 234, 237));
        jtfValorRecebido.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtfValorRecebido.setForeground(new java.awt.Color(113, 113, 113));
        jtfValorRecebido.setToolTipText("Quanto foi recebido");
        jtfValorRecebido.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jtfValorRecebido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfValorRecebidoFocusLost(evt);
            }
        });

        jlValorRecebido.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlValorRecebido.setForeground(new java.awt.Color(113, 113, 113));
        jlValorRecebido.setText("Valor recebido");

        jtfTroco.setBackground(new java.awt.Color(230, 234, 237));
        jtfTroco.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jtfTroco.setForeground(new java.awt.Color(113, 113, 113));
        jtfTroco.setToolTipText("Você deve retornar ao cliente");
        jtfTroco.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jlTroco.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlTroco.setForeground(new java.awt.Color(113, 113, 113));
        jlTroco.setText("Troco");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        iconMulherPhone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/undraw_playful_cat_ql3n.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addComponent(iconMulherPhone)
                .addContainerGap(80, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(iconMulherPhone)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        cbNomeAnimal.setBackground(new java.awt.Color(230, 234, 237));
        cbNomeAnimal.setToolTipText("Selecione o animal");
        cbNomeAnimal.setBorder(null);
        cbNomeAnimal.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbNomeAnimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNomeAnimalActionPerformed(evt);
            }
        });

        jlNomeAnimal.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jlNomeAnimal.setForeground(new java.awt.Color(113, 113, 113));
        jlNomeAnimal.setText("Selecionar Animal");
        jlNomeAnimal.setToolTipText("");

        btnAlterarDados.setBackground(new java.awt.Color(255, 255, 255));
        btnAlterarDados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/shuffle.png"))); // NOI18N
        btnAlterarDados.setToolTipText("Alterar");
        btnAlterarDados.setBorder(null);
        btnAlterarDados.setBorderPainted(false);
        btnAlterarDados.setFocusPainted(false);
        btnAlterarDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarDadosActionPerformed(evt);
            }
        });

        btnLimparCampos1.setBackground(new java.awt.Color(255, 255, 255));
        btnLimparCampos1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete_sign-24.png"))); // NOI18N
        btnLimparCampos1.setToolTipText("Limpar campos");
        btnLimparCampos1.setBorder(null);
        btnLimparCampos1.setBorderPainted(false);
        btnLimparCampos1.setFocusPainted(false);
        btnLimparCampos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparCampos1ActionPerformed(evt);
            }
        });

        btnAnimalADD.setBackground(new java.awt.Color(255, 255, 255));
        btnAnimalADD.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        btnAnimalADD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-corgi-32.png"))); // NOI18N
        btnAnimalADD.setToolTipText("Cadastrar");
        btnAnimalADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimalADDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpBackgroundLayout = new javax.swing.GroupLayout(jpBackground);
        jpBackground.setLayout(jpBackgroundLayout);
        jpBackgroundLayout.setHorizontalGroup(
            jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jpBackgroundLayout.createSequentialGroup()
                .addComponent(jpSideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpBackgroundLayout.createSequentialGroup()
                        .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpBackgroundLayout.createSequentialGroup()
                                .addComponent(jlTipoAtendimento)
                                .addGap(0, 7, Short.MAX_VALUE))
                            .addComponent(cbTipoAtendimento, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jdcDiaAtendimento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(cbNomeAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jlNomeCliente)))
                            .addGroup(jpBackgroundLayout.createSequentialGroup()
                                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlNomeAnimal)
                                    .addGroup(jpBackgroundLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(btnAnimalADD, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 320, Short.MAX_VALUE)))
                        .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbHorario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbMetodoPag, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jpBackgroundLayout.createSequentialGroup()
                                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlTipoAtendimento6)
                                    .addComponent(jlHorario))
                                .addGap(0, 5, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBackgroundLayout.createSequentialGroup()
                        .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfValorRecebido, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlValorRecebido))
                        .addGap(18, 18, 18)
                        .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlTroco)
                            .addComponent(jtfTroco)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBackgroundLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAlterarDados, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimparCampos1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jpBackgroundLayout.setVerticalGroup(
            jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBackgroundLayout.createSequentialGroup()
                .addComponent(jpHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpSideMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpBackgroundLayout.createSequentialGroup()
                        .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpBackgroundLayout.createSequentialGroup()
                                .addGap(111, 111, 111)
                                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jpBackgroundLayout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jpBackgroundLayout.createSequentialGroup()
                                                .addComponent(jlTipoAtendimento)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbTipoAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jpBackgroundLayout.createSequentialGroup()
                                                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jlNomeCliente)
                                                    .addComponent(jlHorario))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(cbHorario)
                                                    .addComponent(cbNomeCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))))
                                        .addGap(11, 11, 11)
                                        .addComponent(jlNomeAnimal)
                                        .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBackgroundLayout.createSequentialGroup()
                                                .addGap(8, 8, 8)
                                                .addComponent(btnAnimalADD, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jpBackgroundLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cbNomeAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jdcDiaAtendimento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(jpBackgroundLayout.createSequentialGroup()
                                        .addComponent(jlTipoAtendimento6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbMetodoPag, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlValorRecebido)
                                    .addComponent(jlTroco))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jtfValorRecebido, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtfTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnLimparCampos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAlterarDados, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnCadastrar, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(btnLimparCampos1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jpBackgroundLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbNomeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNomeClienteActionPerformed
        
    }//GEN-LAST:event_cbNomeClienteActionPerformed

    private void btnLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCamposActionPerformed
       cbTipoAtendimento.setSelectedIndex(0);
       tfValor.setText(null);
       cbStatus.setSelectedIndex(0);
       cbNomeCliente.setSelectedIndex(0);
       cbHorario.setSelectedIndex(0);
       cbMetodoPag.setSelectedIndex(0);
       
    }//GEN-LAST:event_btnLimparCamposActionPerformed

    private void cbTipoAtendimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoAtendimentoActionPerformed
        if(cbTipoAtendimento.getSelectedItem() == "Banho"){
            tfValor.setText("50");
        }else if(cbTipoAtendimento.getSelectedItem() == "Tosa"){
            tfValor.setText("85");
        }if(cbTipoAtendimento.getSelectedItem() == "Banho+Tosa"){
            tfValor.setText("135");
        }
    }//GEN-LAST:event_cbTipoAtendimentoActionPerformed

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        try {
            cadastrar();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Atendimento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void tfValorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfValorActionPerformed

    }//GEN-LAST:event_tfValorActionPerformed

    private void cbMetodoPagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMetodoPagActionPerformed
        if(cbMetodoPag.getSelectedItem() == "Cartão"){
            jtfTroco.setVisible(false);
            jtfValorRecebido.setVisible(false);
            jlValorRecebido.setVisible(false);
            jtfTroco.setVisible(false);
            jlTroco.setVisible(false);
        }else if(cbMetodoPag.getSelectedItem() == "Dinheiro" && tfValor.getText() != null){
            jtfTroco.setVisible(true);
            jtfValorRecebido.setVisible(true);
            jlValorRecebido.setVisible(true);
            jtfTroco.setVisible(true);
            jlTroco.setVisible(true);
        }
    }//GEN-LAST:event_cbMetodoPagActionPerformed

    private void jtfValorRecebidoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfValorRecebidoFocusLost
        if(tfValor.getText() != null || jtfValorRecebido != null){
            pegaTroco();
        }else if(tfValor.getText() == null){
            jtfTroco.setText("");
        }

    }//GEN-LAST:event_jtfValorRecebidoFocusLost

    private void btnMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuMouseClicked
        if(menuDisplay == false){
            try {
                mostrarTabela();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
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
        pesquisarAtendimento();
    }//GEN-LAST:event_jtxPesquisarActionPerformed

    private void jtxPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxPesquisarKeyReleased
        pesquisarAtendimento();
    }//GEN-LAST:event_jtxPesquisarKeyReleased

    private void btnTrazerDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrazerDadosActionPerformed
        try {
            setar();
        } catch (SQLException ex) {
            Logger.getLogger(Atendimento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTrazerDadosActionPerformed

    private void cbNomeAnimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNomeAnimalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNomeAnimalActionPerformed

    private void cbNomeClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbNomeClienteFocusLost
        try {
            pegaAnimal();
        } catch (SQLException ex) {
            Logger.getLogger(Atendimento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cbNomeClienteFocusLost

    private void btnAlterarDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarDadosActionPerformed
        try {
            alterarAtendimento();
        } catch (SQLException ex) {
            Logger.getLogger(Atendimento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAlterarDadosActionPerformed

    private void btnLimparCampos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCampos1ActionPerformed
        int confirma = JOptionPane.showConfirmDialog(null,"Confirme a exclusão dos dados","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            try {
                deletar();
            } catch (SQLException ex) {
                Logger.getLogger(Atendimento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnLimparCampos1ActionPerformed

    private void btnAnimalADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnimalADDActionPerformed
        Animal animal = null;
        try {
            animal = new Animal();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        animal.setVisible(true);
    }//GEN-LAST:event_btnAnimalADDActionPerformed

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
            java.util.logging.Logger.getLogger(Atendimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Atendimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Atendimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Atendimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    try {
                        new Atendimento().setVisible(true);
                    } catch (SQLException ex) {
                        Logger.getLogger(Atendimento.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Atendimento.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterarDados;
    private javax.swing.JButton btnAnimalADD;
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnLimparCampos;
    private javax.swing.JButton btnLimparCampos1;
    private javax.swing.JLabel btnMenu;
    private javax.swing.JButton btnTrazerDados;
    private javax.swing.JComboBox<String> cbHorario;
    private javax.swing.JComboBox<String> cbMetodoPag;
    private javax.swing.JComboBox<Object> cbNomeAnimal;
    private javax.swing.JComboBox<Object> cbNomeCliente;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JComboBox<String> cbTipoAtendimento;
    private javax.swing.JLabel iconMulherPhone;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser jdcDiaAtendimento;
    private javax.swing.JLabel jlHorario;
    private javax.swing.JLabel jlNomeAnimal;
    private javax.swing.JLabel jlNomeCliente;
    private javax.swing.JLabel jlTipoAtendimento;
    private javax.swing.JLabel jlTipoAtendimento6;
    private javax.swing.JLabel jlTipoAtendimento7;
    private javax.swing.JLabel jlTitulo;
    private javax.swing.JLabel jlTroco;
    private javax.swing.JLabel jlValorRecebido;
    private javax.swing.JPanel jpBackground;
    private javax.swing.JPanel jpHeader;
    private javax.swing.JPanel jpMenu;
    private javax.swing.JPanel jpSideMenu;
    private javax.swing.JTable jtbAtendimento;
    private javax.swing.JTextField jtfIDAtendimento;
    private javax.swing.JTextField jtfTroco;
    private javax.swing.JTextField jtfValorRecebido;
    private javax.swing.JTextField jtxPesquisar;
    private javax.swing.JTextField tfValor;
    // End of variables declaration//GEN-END:variables
}
