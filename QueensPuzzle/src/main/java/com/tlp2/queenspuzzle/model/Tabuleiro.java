package com.tlp2.queenspuzzle.model;

/**
 *
 * @author saral
 */

public class Tabuleiro {

    private int tamanho;
    private boolean[][] rainhas;    
    private boolean[][] marcacoes;  
    private int[][] regioes;        

    public Tabuleiro(int tamanho) {
        this.tamanho = tamanho;
        this.rainhas = new boolean[tamanho][tamanho];
        this.marcacoes = new boolean[tamanho][tamanho];
        this.regioes = gerarRegioes(tamanho);
    }

    private int[][] gerarRegioes(int n) {
        int[][] r = new int[n][n];

        if (n == 5) {
            int[][] layout = {
                {0, 0, 1, 1, 1},
                {0, 0, 1, 2, 2},
                {0, 3, 3, 2, 2},
                {4, 3, 3, 3, 2},
                {4, 4, 4, 3, 2}
            };
            return layout;
        }

        if (n == 6) {
            int[][] layout = {
                {0, 0, 0, 1, 1, 1},
                {0, 0, 1, 1, 2, 2},
                {3, 0, 1, 2, 2, 2},
                {3, 3, 4, 4, 2, 5},
                {3, 3, 4, 4, 5, 5},
                {3, 4, 4, 5, 5, 5}
            };
            return layout;
        }

        if (n == 7) {
            int[][] layout = {
                {0, 0, 0, 1, 1, 1, 1},
                {0, 0, 2, 2, 1, 1, 1},
                {0, 2, 2, 2, 3, 3, 1},
                {4, 2, 2, 3, 3, 3, 5},
                {4, 4, 6, 3, 3, 5, 5},
                {4, 4, 6, 6, 5, 5, 5},
                {4, 6, 6, 6, 6, 5, 5}
            };
            return layout;
        }
        
        int[][] layout = {
            {0, 0, 0, 1, 1, 1, 2, 2},
            {0, 0, 1, 1, 1, 2, 2, 2},
            {0, 3, 3, 1, 2, 2, 2, 4},
            {3, 3, 3, 5, 5, 2, 4, 4},
            {3, 3, 5, 5, 5, 6, 4, 4},
            {3, 7, 5, 5, 6, 6, 6, 4},
            {7, 7, 7, 5, 6, 6, 4, 4},
            {7, 7, 7, 7, 6, 6, 4, 4}
        };
        return layout;
    }

    public void alternarRainha(int linha, int coluna) {
        rainhas[linha][coluna] = !rainhas[linha][coluna];
        if (rainhas[linha][coluna]) {
            marcacoes[linha][coluna] = false; 
        }
    }

    public void alternarMarcacao(int linha, int coluna) {
        if (!rainhas[linha][coluna]) {
            marcacoes[linha][coluna] = !marcacoes[linha][coluna];
        }
    }

    public void colocarMarcacao(int linha, int coluna) {
        if (!rainhas[linha][coluna]) {
            marcacoes[linha][coluna] = true;
        }
    }

    public boolean temMarcacao(int linha, int coluna) {
        return marcacoes[linha][coluna];
    }

    public boolean temRainha(int linha, int coluna) {
        return rainhas[linha][coluna];
    }

    public int getRegiao(int linha, int coluna) {
        return regioes[linha][coluna];
    }

    public int contarRainhas() {
        int total = 0;
        for (int i = 0; i < tamanho; i++)
            for (int j = 0; j < tamanho; j++)
                if (rainhas[i][j]) total++;
        return total;
    }

    public boolean estaEmConflito(int linha, int coluna) {
        if (!rainhas[linha][coluna]) return false;

        int regiao = regioes[linha][coluna];

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (i == linha && j == coluna) continue;
                if (!rainhas[i][j]) continue;

                if (i == linha) return true;

                if (j == coluna) return true;

                if (Math.abs(i - linha) <= 1 && Math.abs(j - coluna) <= 1) return true;

                if (regioes[i][j] == regiao) return true;
            }
        }
        return false;
    }

    public boolean cadaRegiaоTemUmaRainha() {
        int[] contagem = new int[tamanho];
        for (int i = 0; i < tamanho; i++)
            for (int j = 0; j < tamanho; j++)
                if (rainhas[i][j])
                    contagem[regioes[i][j]]++;

        for (int c : contagem)
            if (c != 1) return false;
        return true;
    }

    public boolean estaSolucionado() {
        if (contarRainhas() != tamanho) return false;
        if (!cadaRegiaоTemUmaRainha()) return false;

        for (int i = 0; i < tamanho; i++)
            for (int j = 0; j < tamanho; j++)
                if (rainhas[i][j] && estaEmConflito(i, j))
                    return false;

        return true;
    }

    public void limpar() {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                rainhas[i][j] = false;
                marcacoes[i][j] = false;
            }
        }
    }

    public int[] getDica() {
        boolean[][] solucao = new boolean[tamanho][tamanho];
        if (resolverBacktracking(solucao, new boolean[tamanho], new boolean[tamanho], new boolean[tamanho], 0)) {
            for (int i = 0; i < tamanho; i++)
                for (int j = 0; j < tamanho; j++)
                    if (solucao[i][j] && !rainhas[i][j])
                        return new int[]{i, j};
        }
        return null;
    }

 
    private boolean resolverBacktracking(boolean[][] sol, boolean[] linhaUsada, boolean[] colunaUsada, boolean[] regiaoUsada, int regiao) {
        if (regiao == tamanho) return true;

        for (int i = 0; i < tamanho; i++) {
            if (linhaUsada[i]) continue;
            for (int j = 0; j < tamanho; j++) {
                if (colunaUsada[j]) continue;
                if (regioes[i][j] != regiao) continue;

                boolean adjacente = false;
                for (int r = 0; r < tamanho && !adjacente; r++)
                    for (int c = 0; c < tamanho && !adjacente; c++)
                        if (sol[r][c] && Math.abs(r - i) <= 1 && Math.abs(c - j) <= 1)
                            adjacente = true;

                if (adjacente) continue;

                sol[i][j] = true;
                linhaUsada[i] = true;
                colunaUsada[j] = true;

                if (resolverBacktracking(sol, linhaUsada, colunaUsada, regiaoUsada, regiao + 1))
                    return true;

                sol[i][j] = false;
                linhaUsada[i] = false;
                colunaUsada[j] = false;
            }
        }
        return false;
    }

    public int getTamanho() { 
        return tamanho; 
    }
}