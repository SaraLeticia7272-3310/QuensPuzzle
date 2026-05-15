package com.tlp2.nightfallz.Model;

import java.util.ArrayList;
import java.util.List;

public class inventario {

    private List<item> itens;

    public inventario() {
        itens = new ArrayList<>();
    }

    public void adicionarItem(item item) {
        itens.add(item);
    }

    public void removerItem(item item) {
        itens.remove(item);
    }

    public List<item> getItens() {
        return itens;
    }
}