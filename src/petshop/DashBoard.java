package petshop;

import static java.awt.AWTEventMulticaster.add;
import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Victor - LiceuTec
 */
public final class DashBoard extends javax.swing.JFrame {

    /**
     * Creates new form DashBoard
     */
    Connection con = null;
    PreparedStatement ps = null, ps1 = null, ps2 = null;
    ResultSet rs = null, rs1 = null, rs2 = null;
    int qtdGatos = 0, qtdCachorros = 0;
    
    
    public DashBoard() throws ClassNotFoundException, SQLException {
        initComponents();        
        mostrarTabela();
        criaGraficoBarras();
        quantidadeClientes();
        quantidadeGato();
        quantidadeCachorro();
        
    }
    
    
    public void criaGraficoBarras() throws SQLException{
        float BanhoTosa = 0, Tosa = 0;
        float v = 0,v1 = 0,v2 = 0;//Valor Total de cada
        
        
        //BANHO + TOSA -> Calculando
        String SQL = "select SUM(valorAtendimento) from atendimento where tipoAtendimento = 'Banho+Tosa'";
        ps = con.prepareStatement(SQL);
        rs = ps.executeQuery();
        while (rs.next()) {            
            float c = rs.getInt(1);
            v = v + c;
        }
        
        
        //BANHO -> Calculando
        String SQL1 = "select SUM(valorAtendimento) from atendimento where tipoAtendimento = 'Banho'";
        ps1 = con.prepareStatement(SQL1);
        rs1 = ps1.executeQuery();
        while (rs1.next()) {            
            float c = rs1.getInt(1);
            v1 = v1 + c;
        }
        
        
        
        //TOSA -> Calculando
        String SQL2 = "select SUM(valorAtendimento) from atendimento where tipoAtendimento = 'Tosa'";
        ps2 = con.prepareStatement(SQL2);
        rs2 = ps2.executeQuery();
        while (rs2.next()) {            
            float c = rs2.getInt(1);
            v2 = v2 + c;
        }
        
        
        
        DefaultCategoryDataset barra = new DefaultCategoryDataset();
        barra.setValue(v,  "", "Banho+Tosa");
        barra.setValue(v1, "", "Banho");
        barra.setValue(v2, "", "Tosa");
        
        JFreeChart grafico = ChartFactory.createBarChart("Faturamento em Reais","","", barra, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot barraItem = grafico.getCategoryPlot();
        barraItem.getRenderer().setSeriesPaint(0, new Color(112,99,255));
        barraItem.getRenderer().setSeriesPaint(1, new Color(112,99,255));
        barraItem.getRenderer().setSeriesPaint(2, new Color(112,99,255));
        ChartPanel CP = new ChartPanel(grafico);
        JPChart.add(CP,BorderLayout.CENTER);
        JPChart.validate();
    }
    public void pesquisarAnimais(){
        String SQL = "SELECT status AS Status, tipoAtendimento AS 'Tipo de Atendimento',"
                + "dataAtendimento AS Data, horarioInicio AS Horario, valorAtendimento AS Valor,"
                + "c.nome AS Nome,a.nome AS Animal,cod_cliente,cod_animal  FROM atendimento "
                + "INNER JOIN cliente c ON cod_cliente = id_cliente"
                + " INNER JOIN animal a ON cod_animal = id_animal "
                + "where a.nome LIKE ? OR tipoAtendimento LIKE ? OR status LIKE ? OR c.nome LIKE ?";
        
//        String SQL = "SELECT id_animal AS ID ,nome AS Nome,tipo AS Tipo,raca AS Raça,porte AS Porte,"
//                + "cod_dono from animal, cliente WHERE nome LIKE ? OR raca like ?"
//                + "  UNION SELECT nome AS 'Nome Dono',cpf AS CPF FROM cliente WHERE nome LIKE ? OR cpf LIKE ?";
        
        try{
            ps = con.prepareStatement(SQL);
            ps.setString(1, jtxPesquisar.getText()+"%");
            ps.setString(2, jtxPesquisar.getText()+"%");
            ps.setString(3, jtxPesquisar.getText()+"%");
            ps.setString(4, jtxPesquisar.getText()+"%");
            rs = ps.executeQuery();
            jtlDashBoard.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    // TESTE de GIT
    // 2
    public void quantidadeGato() throws ClassNotFoundException, SQLException{
        String SQL = "select * from animal where tipo like '%Gato%'";
        
        try{
            ps = con.prepareStatement(SQL);
            rs = ps.executeQuery();
            rs.last();
            qtdGatos = rs.getRow();
            jlQtdGato.setText(Integer.toString(qtdGatos));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    
    public void quantidadeCachorro() throws ClassNotFoundException, SQLException{
        String SQL = "select * from animal where tipo like '%Cachorro%'";
        
        try{
            ps = con.prepareStatement(SQL);
            rs = ps.executeQuery();
            rs.last();
            qtdCachorros = rs.getRow();
            jlQtdCachorro.setText(Integer.toString(qtdCachorros));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        }
    }
    public void quantidadeClientes(){
        String sql = "SELECT * from cliente";
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.last();
            int qtdClientes = rs.getRow();
            jlQtdClientes.setText(Integer.toString(qtdClientes));
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error);
        } 
    }
    
//    CODIGOS DA TABELA
      // NESTE CODIGO COMENTADO A FUNÇÃO É CLICAR NA TABELA E APARECER EM TEXT FIELDS....   
//    public void setar(){;
//        int setar = jtlDashBoard.getSelectedRow();
//        JTextField.setText(jtlDashBoard.getModel().getValueAt(setar,0).toString());
//    }
    public void mostrarTabela() throws ClassNotFoundException{
        String SQL = "SELECT status AS Status, tipoAtendimento AS 'Tipo de Atendimento',"
                + "dataAtendimento AS Data, horarioInicio AS Horario, valorAtendimento AS Valor,"
                + "c.nome AS Nome,a.nome AS Animal,cod_cliente,cod_animal  FROM atendimento "
                + "INNER JOIN cliente c ON cod_cliente = id_cliente"
                + " INNER JOIN animal a ON cod_animal = id_animal";
        
        
//        String SQL = "SELECT id_cliente AS 'ID Cliente', id_animal AS 'ID Animal',a.nome AS Nome,tipo AS Tipo,raca AS Raça,porte AS Porte,"
//                + "cod_dono,cod_cliente, c.nome, at.tipoAtendimento AS 'Tipo Atendimento', valorAtendimento AS 'Sub total' "
//                + "from animal a, cliente c, atendimento at WHERE a.cod_dono = id_cliente AND cod_cliente = id_cliente";
        con = Conectabd.conectabd();
        try{
            ps = con.prepareStatement(SQL);
            rs = ps.executeQuery();
            jtlDashBoard.setModel(DbUtils.resultSetToTableModel(rs));
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

        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel3 = new javax.swing.JPanel();
        JPTopo = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlQtdGato = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jlQtdCachorro = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jlQtdClientes = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnRelatorio = new javax.swing.JLabel();
        btnRelatorio1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtlDashBoard = new javax.swing.JTable();
        JPChart = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtxPesquisar = new javax.swing.JTextField();
        iconRefresh = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMCliente = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jmAnimal = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jmAtendimento = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        JPTopo.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(248, 248, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(159, 158));

        jlQtdGato.setBackground(new java.awt.Color(255, 255, 255));
        jlQtdGato.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jlQtdGato.setForeground(new java.awt.Color(186, 184, 229));
        jlQtdGato.setText("0");
        jlQtdGato.setToolTipText("");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_Cat_48px_7.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jlQtdGato)
                    .addComponent(jLabel2))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlQtdGato)
                .addGap(28, 28, 28))
        );

        jPanel6.setBackground(new java.awt.Color(248, 248, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(158, 158));

        jlQtdCachorro.setBackground(new java.awt.Color(255, 255, 255));
        jlQtdCachorro.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jlQtdCachorro.setForeground(new java.awt.Color(186, 184, 229));
        jlQtdCachorro.setText("0");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_Dog_48px_3.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(60, 60, 60))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jlQtdCachorro)
                        .addGap(75, 75, 75))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jlQtdCachorro)
                .addGap(28, 28, 28))
        );

        jPanel8.setBackground(new java.awt.Color(248, 248, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(158, 158));

        jlQtdClientes.setBackground(new java.awt.Color(255, 255, 255));
        jlQtdClientes.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jlQtdClientes.setForeground(new java.awt.Color(186, 184, 229));
        jlQtdClientes.setText("0");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/multiple-users-silhouette.png"))); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(85, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jlQtdClientes)
                    .addComponent(jLabel11))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(47, 47, 47)
                .addComponent(jlQtdClientes)
                .addGap(36, 36, 36))
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/PetLove.png"))); // NOI18N

        btnRelatorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/clipboard.png"))); // NOI18N
        btnRelatorio.setText(" ");
        btnRelatorio.setToolTipText("Gerar relatório");
        btnRelatorio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRelatorioMouseClicked(evt);
            }
        });

        btnRelatorio1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/conversation.png"))); // NOI18N
        btnRelatorio1.setText(" ");
        btnRelatorio1.setToolTipText("Gerar relatório");
        btnRelatorio1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRelatorio1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout JPTopoLayout = new javax.swing.GroupLayout(JPTopo);
        JPTopo.setLayout(JPTopoLayout);
        JPTopoLayout.setHorizontalGroup(
            JPTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPTopoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRelatorio1)
                .addGap(18, 18, 18)
                .addComponent(btnRelatorio)
                .addContainerGap())
        );
        JPTopoLayout.setVerticalGroup(
            JPTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPTopoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JPTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPTopoLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPTopoLayout.createSequentialGroup()
                        .addGroup(JPTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                        .addContainerGap(17, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPTopoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JPTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRelatorio)
                    .addComponent(btnRelatorio1))
                .addGap(68, 68, 68))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jtlDashBoard.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, "", null, null}
            },
            new String [] {
                "Tipo", "Valor", "Atendimento", "Nome do Animal", "Dono"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jtlDashBoard.setGridColor(new java.awt.Color(204, 204, 204));
        jtlDashBoard.setSelectionBackground(new java.awt.Color(112, 99, 255));
        jScrollPane1.setViewportView(jtlDashBoard);

        JPChart.setLayout(new java.awt.BorderLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-search-48.png"))); // NOI18N

        jtxPesquisar.setBackground(new java.awt.Color(230, 234, 237));
        jtxPesquisar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jtxPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxPesquisarKeyReleased(evt);
            }
        });

        iconRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_Refresh_48px_1.png"))); // NOI18N
        iconRefresh.setToolTipText("Atualizar Tabela e Grafico");
        iconRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconRefreshMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtxPesquisar)
                        .addGap(18, 18, 18)
                        .addComponent(iconRefresh)))
                .addGap(18, 18, 18)
                .addComponent(JPChart, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JPChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(iconRefresh)
                            .addComponent(jLabel1)
                            .addComponent(jtxPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPTopo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(JPTopo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMCliente.setText("Cliente");

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/all.png"))); // NOI18N
        jMenuItem3.setText("Ver");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMCliente.add(jMenuItem3);

        jMenuBar1.add(jMCliente);

        jmAnimal.setText("Animal");
        jmAnimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmAnimalActionPerformed(evt);
            }
        });

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/all.png"))); // NOI18N
        jMenuItem1.setText("Ver");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jmAnimal.add(jMenuItem1);

        jMenuBar1.add(jmAnimal);

        jmAtendimento.setText("Atendimento");

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/all.png"))); // NOI18N
        jMenuItem4.setText("Ver");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jmAtendimento.add(jMenuItem4);

        jMenuBar1.add(jmAtendimento);

        jMenu1.setText("Funcionário");

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/all.png"))); // NOI18N
        jMenuItem5.setText("Ver");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void iconRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconRefreshMouseClicked
        try {
            mostrarTabela();
            quantidadeGato();
            quantidadeCachorro();
            criaGraficoBarras();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_iconRefreshMouseClicked

    private void jtxPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxPesquisarKeyReleased
        pesquisarAnimais();
    }//GEN-LAST:event_jtxPesquisarKeyReleased

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Cliente cliente = null;
        try {
            cliente = new Cliente();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        cliente.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void btnRelatorioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRelatorioMouseClicked
        try {
            con = Conectabd.conectabd();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        int confirma = JOptionPane.showConfirmDialog(null,"Confirme a impressão deste relatório","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            
            try {
                JasperPrint print = JasperFillManager.fillReport("Relatorios\\relatorio.jasper",null,con);
                JasperViewer viewer = new JasperViewer(print, false);
                viewer.show();
                viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_btnRelatorioMouseClicked

    private void btnRelatorio1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRelatorio1MouseClicked
        try {
            con = Conectabd.conectabd();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        int confirma = JOptionPane.showConfirmDialog(null,"Confirme a impressão deste relatório","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            
            try {
                JasperPrint print = JasperFillManager.fillReport("Relatorios\\Clientes.jasper",null,con);
                JasperViewer viewer = new JasperViewer(print, false);
                viewer.show();
                viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_btnRelatorio1MouseClicked

    private void jmAnimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmAnimalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jmAnimalActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Animal animal = null;
        try {
            animal = new Animal();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        animal.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        Atendimento atendimento = null;
        try {
            atendimento = new Atendimento();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        atendimento.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        Funcionario funcionario = null;
        try {
            funcionario = new Funcionario();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        funcionario.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

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
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new DashBoard().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPChart;
    private javax.swing.JPanel JPTopo;
    private javax.swing.JLabel btnRelatorio;
    private javax.swing.JLabel btnRelatorio1;
    private javax.swing.JLabel iconRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMCliente;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlQtdCachorro;
    private javax.swing.JLabel jlQtdClientes;
    private javax.swing.JLabel jlQtdGato;
    private javax.swing.JMenu jmAnimal;
    private javax.swing.JMenu jmAtendimento;
    private javax.swing.JTable jtlDashBoard;
    private javax.swing.JTextField jtxPesquisar;
    // End of variables declaration//GEN-END:variables
}
