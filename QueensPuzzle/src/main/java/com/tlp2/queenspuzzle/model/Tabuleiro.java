package com.tlp2.queenspuzzle.model;

public class Tabuleiro {

    private int tamanho;          
    private boolean[][] celulas;  

    public Tabuleiro(int tamanho) {
        this.tamanho = tamanho;
        this.celulas = new boolean[tamanho][tamanho];
    }

    public void alternarRainha(int linha, int coluna) {
        celulas[linha][coluna] = !celulas[linha][coluna];
    }

    public boolean temRainha(int linha, int coluna) {
        return celulas[linha][coluna];
    }

    public int contarRainhas() {
        int total = 0;
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (celulas[i][j]) total++;
            }
        }
        return total;
    }

    public boolean estaEmConflito(int linha, int coluna) {
        for (int j = 0; j < tamanho; j++) {
           
            if (j != coluna && celulas[linha][j]) return true;
        }
        for (int i = 0; i < tamanho; i++) {
            
            if (i != linha && celulas[i][coluna]) return true;
        }
        
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (i == linha && j == coluna) continue;
                if (celulas[i][j]) {
                    if (Math.abs(i - linha) == Math.abs(j - coluna)) return true;
                }
            }
        }
        return false;
    }

    public boolean estaSolucionado() {
        if (contarRainhas() != tamanho) return false;

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (celulas[i][j] && estaEmConflito(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void limpar() {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                celulas[i][j] = false;
            }
        }
    }

    public int[] getDica() {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (!celulas[i][j]) {
                    
                    celulas[i][j] = true;
                    if (!estaEmConflito(i, j)) {
                        celulas[i][j] = false;
                        return new int[]{i, j};
                    }
                    celulas[i][j] = false;
                }
            }
        }
        return null;
    }

    public int getTamanho() { 
        return tamanho; 
    }
}
