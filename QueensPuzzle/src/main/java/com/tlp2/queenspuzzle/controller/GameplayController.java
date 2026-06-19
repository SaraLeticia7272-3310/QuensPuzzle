package com.tlp2.queenspuzzle.controller;

import com.tlp2.queenspuzzle.dao.JogadorDAO;
import com.tlp2.queenspuzzle.dao.PartidaDAO;
import com.tlp2.queenspuzzle.model.*;
import com.tlp2.queenspuzzle.MainApp;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller da tela de Gameplay.
 * Mecânica: regiões coloridas no tabuleiro, 1 rainha por região,
 * sem rainhas na mesma linha, coluna ou célula adjacente.
 */
public class GameplayController implements Initializable {

    @FXML 
    private GridPane gridTabuleiro;
    
    @FXML 
    private Label labelNome;
    
    @FXML 
    private Label labelNivel;
    
    @FXML 
    private Label labelPontos;
    
    @FXML 
    private Label labelRainhas;
    
    @FXML 
    private Label labelTempo;
    
    @FXML 
    private Label labelMensagem;
    
    @FXML 
    private Label labelMoedas;
    
    @FXML 
    private Button btnDica;

    private Tabuleiro tabuleiro;
    private SessaoJogo sessao;
    private int tempoRestante;
    private Timeline timer;
    private int dicasUsadas;
    private int maxDicas;

    // Controle de arrastar para marcar X
    private boolean arrastando = false;
    private Boolean modoDrag = null; // true = marcando X, false = desmarcando X

    // Paleta de cores para as regiões (suave e bonita)
    private static final String[] CORES_REGIAO = {
        "#c9e4ff",  // azul suave
        "#bbc5ff",  // lilás suave
        "#d8b7fe",  // roxo suave
        "#fcc5fe",  // rosa suave
        "#ef82db",  // pink vibrante
        "#a8d8ea",  // azul-claro
        "#b5ead7",  // menta
        "#ffdac1"   // pêssego
    };

    // Cores mais escuras para borda das regiões
    private static final String[] BORDAS_REGIAO = {
        "#7ab8e8",
        "#8b9fe8",
        "#b088e8",
        "#e099e8",
        "#c050b0",
        "#70adc0",
        "#80c0a0",
        "#e0a080"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sessao = SessaoJogo.getInstance();
        sessao.iniciarNovaRun();

        // Usa nivelAtual da sessão (setado pelo UpgradesController ao jogar novamente)
        // Na primeira vez (vindo do menu), nivelAtual é 4 → forçamos mínimo 5
        int nivelSessao = sessao.getNivelAtual();
        int tamanho = Math.max(5, Math.min(nivelSessao, 8));
        sessao.setNivelAtual(tamanho);

        tabuleiro = new Tabuleiro(tamanho);
        maxDicas = 1 + sessao.getUpgradeAtual().getDicasExtras();
        dicasUsadas = 0;
        tempoRestante = 90 + sessao.getUpgradeAtual().getTempoBonus();

        atualizarInfos();
        desenharTabuleiro();
        iniciarTimer();
    }

    /**
     * Desenha o tabuleiro com regiões coloridas.
     */
    private void desenharTabuleiro() {
        gridTabuleiro.getChildren().clear();

        int n = tabuleiro.getTamanho();
        double tamanho = 340.0 / n;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int linha = i;
                final int col = j;

                StackPane celula = criarCelula(linha, col, tamanho, n);

                // Clique simples / duplo
                celula.setOnMouseClicked(e -> {
                    if (!arrastando) {
                        if (e.getClickCount() == 2) {
                            aoDuploCliqueCelula(linha, col);
                        } else {
                            aoCliqueSimplesCelula(linha, col);
                        }
                    }
                    arrastando = false;
                });

                // Início do arrastar — define o modo (marcar ou desmarcar X)
                celula.setOnMousePressed(e -> {
                    arrastando = false;
                    // Determina o modo baseado no estado atual da célula
                    if (!tabuleiro.temRainha(linha, col)) {
                        modoDrag = !tabuleiro.temMarcacao(linha, col); // true = vai marcar, false = vai desmarcar
                    } else {
                        modoDrag = null; // célula com rainha: sem drag
                    }
                });

                // Ao arrastar sobre a célula — aplica X
                celula.setOnMouseDragEntered(e -> {
                    arrastando = true;
                    if (modoDrag != null && !tabuleiro.temRainha(linha, col)) {
                        boolean temMarcacao = tabuleiro.temMarcacao(linha, col);
                        if (modoDrag && !temMarcacao) {
                            tabuleiro.alternarMarcacao(linha, col);
                            desenharTabuleiro();
                        } else if (!modoDrag && temMarcacao) {
                            tabuleiro.alternarMarcacao(linha, col);
                            desenharTabuleiro();
                        }
                    }
                });

                // Habilita o drag source nesta célula
                celula.setOnDragDetected(e -> {
                    celula.startFullDrag();
                    arrastando = true;
                });
                gridTabuleiro.add(celula, j, i);
            }
        }
    }

    /**
     * Cria uma célula com cor da região e bordas internas entre regiões.
     */
    private StackPane criarCelula(int linha, int col, double tam, int n) {
        StackPane celula = new StackPane();

        int regiao = tabuleiro.getRegiao(linha, col);
        String corFundo = CORES_REGIAO[regiao % CORES_REGIAO.length];
        String corBorda = BORDAS_REGIAO[regiao % BORDAS_REGIAO.length];

        // Fundo colorido da região
        Rectangle fundo = new Rectangle(tam, tam);
        fundo.setFill(Color.web(corFundo));

        // Bordas: mais grossas onde muda de região (efeito de contorno)
        double bTop    = (linha == 0 || tabuleiro.getRegiao(linha-1, col) != regiao) ? 2.5 : 0.5;
        double bBottom = (linha == n-1 || tabuleiro.getRegiao(linha+1, col) != regiao) ? 2.5 : 0.5;
        double bLeft   = (col == 0 || tabuleiro.getRegiao(linha, col-1) != regiao) ? 2.5 : 0.5;
        double bRight  = (col == n-1 || tabuleiro.getRegiao(linha, col+1) != regiao) ? 2.5 : 0.5;

        // Usa um Rectangle com stroke uniforme e depois sobrepõe linhas de borda
        fundo.setStroke(Color.web(corBorda));
        fundo.setStrokeWidth(0.5);

        celula.getChildren().add(fundo);

        // Linhas de borda espessa onde muda região
        if (bTop > 1) {
            Line l = new Line(0, 0, tam, 0);
            l.setStroke(Color.web(corBorda));
            l.setStrokeWidth(bTop);
            celula.getChildren().add(l);
            StackPane.setAlignment(l, javafx.geometry.Pos.TOP_LEFT);
        }
        if (bBottom > 1) {
            Line l = new Line(0, tam - bBottom/2, tam, tam - bBottom/2);
            l.setStroke(Color.web(corBorda));
            l.setStrokeWidth(bBottom);
            celula.getChildren().add(l);
            StackPane.setAlignment(l, javafx.geometry.Pos.TOP_LEFT);
        }
        if (bLeft > 1) {
            Line l = new Line(0, 0, 0, tam);
            l.setStroke(Color.web(corBorda));
            l.setStrokeWidth(bLeft);
            celula.getChildren().add(l);
            StackPane.setAlignment(l, javafx.geometry.Pos.TOP_LEFT);
        }
        if (bRight > 1) {
            Line l = new Line(tam - bRight/2, 0, tam - bRight/2, tam);
            l.setStroke(Color.web(corBorda));
            l.setStrokeWidth(bRight);
            celula.getChildren().add(l);
            StackPane.setAlignment(l, javafx.geometry.Pos.TOP_LEFT);
        }
        
        // Marcação X
        if (tabuleiro.temMarcacao(linha, col)) {

            Text x = new Text("❌");

            double fontSize = tam * 0.42;

            x.setStyle(
                "-fx-font-size: " + fontSize + ";"
            );

            celula.getChildren().add(x);
        }

        // Se tem rainha, desenha coroa (símbolo unicode bonito)
        if (tabuleiro.temRainha(linha, col)) {
            boolean conflito = tabuleiro.estaEmConflito(linha, col);

            // Circulo de fundo da coroa
            double r = tam * 0.38;
            Circle circulo = new Circle(r);
            circulo.setFill(conflito ? Color.web("#ff4444aa") : Color.web("#ffffff99"));
            circulo.setStroke(conflito ? Color.web("#cc0000") : Color.web("#7b4f00"));
            circulo.setStrokeWidth(1.5);

            // Coroa
            Text coroa = new Text("👑");
            double fontSize = tam * 0.48;
            coroa.setStyle("-fx-font-size: " + fontSize + ";");
            coroa.setFill(conflito ? Color.web("#ffffff") : Color.web("#c8860a"));

            celula.getChildren().addAll(circulo, coroa);
        }

        // Efeito hover
        celula.setStyle("-fx-cursor: hand;");
        celula.setOnMouseEntered(e -> fundo.setOpacity(0.82));
        celula.setOnMouseExited(e -> fundo.setOpacity(1.0));

        return celula;
    }

    private void aoCliqueSimplesCelula(int linha, int col) {

        // Rainha -> remove
        if (tabuleiro.temRainha(linha, col)) {

            tabuleiro.alternarRainha(linha, col);

        } else {

            // vazio <-> ❌
            tabuleiro.alternarMarcacao(linha, col);
        }

        desenharTabuleiro();
        atualizarInfos();
    }
    
    private void aoDuploCliqueCelula(int linha, int col) {
        // já existe rainha -> remove
        if (tabuleiro.temRainha(linha, col)) {

            tabuleiro.alternarRainha(linha, col);

        } else {

            // remove ❌ caso exista
            if (tabuleiro.temMarcacao(linha, col)) {
                tabuleiro.alternarMarcacao(linha, col);
            }

            // coloca 👑
            tabuleiro.alternarRainha(linha, col);
        }

        desenharTabuleiro();
        atualizarInfos();

        if (tabuleiro.estaSolucionado()) {

            timer.stop();

            int pontos = calcularPontos();

            sessao.adicionarPontos(pontos);
            sessao.adicionarMoedas(pontos / 10);

            sessao.adicionarItem(
                    new Item(
                            "Coroa Real",
                            "Completou nível "
                            + tabuleiro.getTamanho()
                            + "x"
                            + tabuleiro.getTamanho(),
                            1
                    )
            );

            labelMensagem.setText(
                    "✓ Perfeito! +" + pontos + " pontos!"
            );

            labelMensagem.setStyle(
                    "-fx-text-fill: #27ae60;"
                    + "-fx-font-weight: bold;"
                    + "-fx-font-size: 15;"
            );

            atualizarInfos();

            Timeline espera = new Timeline(
                    new KeyFrame(
                            Duration.seconds(2),
                            e -> {
                                salvarPartida();
                                MainApp.trocarTela(
                                        "/com/tlp2/queenspuzzle/view/Upgrades.fxml"
                                );
                            }
                    )
            );

            espera.play();
        }
    }

    private int calcularPontos() {
        int base = tabuleiro.getTamanho() * 100;
        int bonusTempo = tempoRestante * 5;
        int bonusUpgrade = sessao.getUpgradeAtual().getPontosBonus();
        return base + bonusTempo + bonusUpgrade;
    }

    private void iniciarTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tempoRestante--;
            labelTempo.setText(tempoRestante + "s");

            if (tempoRestante <= 15) {
                labelTempo.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            }

            if (tempoRestante <= 0) {
                timer.stop();
                labelMensagem.setText("Tempo esgotado!");
                labelMensagem.setStyle("-fx-text-fill: #e74c3c;");
                salvarPartida();
                Timeline espera = new Timeline(new KeyFrame(Duration.seconds(2), ev ->
                        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Upgrades.fxml")));
                espera.play();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void atualizarInfos() {
        Jogador j = sessao.getJogadorAtual();
        labelNome.setText("♟ " + j.getNome());
        labelNivel.setText("Nível " + tabuleiro.getTamanho() + "×" + tabuleiro.getTamanho());
        labelPontos.setText("Pontos: " + sessao.getPontuacaoRodada());
        labelMoedas.setText("Moedas: " + sessao.getMoedas());
        labelRainhas.setText("Rainhas: " + tabuleiro.contarRainhas() + "/" + tabuleiro.getTamanho());
        btnDica.setText("Dica (" + (maxDicas - dicasUsadas) + ")");
    }

    private void salvarPartida() {
        Jogador jogador = sessao.getJogadorAtual();
        int pontos = sessao.getPontuacaoRodada();
        jogador.setPontuacaoTotal(jogador.getPontuacaoTotal() + pontos);

        if (tabuleiro.estaSolucionado() && tabuleiro.getTamanho() >= jogador.getNivelMaximo()) {
            jogador.setNivelMaximo(Math.min(jogador.getNivelMaximo() + 1, 8));
        }

        new JogadorDAO().atualizar(jogador);
        new PartidaDAO().salvar(new Partida(jogador.getId(), pontos, tabuleiro.getTamanho()));
    }

    @FXML
    private void aoClicarDica() {
        if (dicasUsadas >= maxDicas) {
            labelMensagem.setText("Sem dicas disponíveis!");
            return;
        }
        int[] dica = tabuleiro.getDica();
        if (dica != null) {
            labelMensagem.setText("Dica: linha " + (dica[0] + 1) + ", coluna " + (dica[1] + 1));
            dicasUsadas++;
            atualizarInfos();
        } else {
            labelMensagem.setText("Nenhuma dica encontrada.");
        }
    }

    @FXML
    private void aoClicarLimpar() {
        tabuleiro.limpar();
        desenharTabuleiro();
        labelMensagem.setText("");
        atualizarInfos();
    }

    @FXML
    private void aoClicarInventario() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/tlp2/queenspuzzle/view/Inventario.fxml"));
            javafx.scene.Parent root = loader.load();

            // Conecta o callback: quando o jogador usar um item, aplica o efeito aqui
            InventarioController invCtrl = loader.getController();
            invCtrl.setOnItemUsado(item -> aplicarEfeitoItem(item));

            javafx.stage.Stage stageInv = new javafx.stage.Stage();
            stageInv.setTitle("Inventário");
            stageInv.setScene(new javafx.scene.Scene(root, 420, 400));
            stageInv.initOwner(MainApp.getStagePrincipal());
            stageInv.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir inventário: " + e.getMessage());
        }
    }

    /**
     * Aplica o efeito do item usado no inventário durante o jogo.
     */
    private void aplicarEfeitoItem(Item item) {
        switch (item.getTipo()) {
            case POCAO_TEMPO:
                tempoRestante += 30;
                labelTempo.setText(tempoRestante + "s");
                // Remove o estilo de urgência se o tempo aumentou
                if (tempoRestante > 15) {
                    labelTempo.setStyle("");
                }
                labelMensagem.setText("🕛 +30 segundos!");
                labelMensagem.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                break;
            case CHAVE_DICA:
                maxDicas++;
                atualizarInfos();
                labelMensagem.setText("🗝 +1 dica disponível!");
                labelMensagem.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                break;
            case AMULETO_PONTOS:
                sessao.adicionarPontos(100);
                atualizarInfos();
                labelMensagem.setText("💎 +100 pontos!");
                labelMensagem.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                break;
            default:
                break;
        }
    }

    @FXML
    private void aoClicarLoja() {
        if (timer != null) timer.stop();
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/Loja.fxml");
    }

    @FXML
    private void aoClicarMenu() {
        if (timer != null) timer.stop();
        MainApp.trocarTela("/com/tlp2/queenspuzzle/view/MenuPrincipal.fxml");
    }
}
